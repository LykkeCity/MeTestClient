package com.lykke.me.test.client.service.impl

import com.lykke.me.test.client.entity.TestMethodEntity
import com.lykke.me.test.client.entity.TestSessionEntity
import com.lykke.me.test.client.service.TestsRunnerService
import com.lykke.me.test.client.service.RunTestsPolicy
import com.lykke.me.test.client.utils.Transformer
import com.lykke.me.test.client.web.dto.TestSessionsDto
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.lang.reflect.InvocationTargetException
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Future

@Component
class TestsRunnerServiceImpl : TestsRunnerService {

    companion object {
        private val logger = Logger.getLogger(TestsRunnerServiceImpl::class.java)
    }

    private val runTestStrategyByRunTestsPolicy = mapOf(RunTestsPolicy.CONTINUE_ON_ERROR to { test: () -> Unit ->
        try {
            test.invoke()
        } catch (e: InterruptedException) {
            throw e
        } catch (e: Exception) {
            logger.error("Error running test", e)
        }
    },
            RunTestsPolicy.STOP_ON_ERROR to { test: () -> Unit ->
                {
                    test.invoke()
                }
            })

    private val testFutureBySessionId = ConcurrentHashMap<String, Future<*>>()
    private val testSessionInformationBySessionId = ConcurrentHashMap<String, TestSessionEntity>()

    @Autowired
    private lateinit var testRunnerThreadPool: ThreadPoolTaskExecutor

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    override fun run(testMethods: List<TestMethodEntity>, runPolicy: RunTestsPolicy): String {
        val sessionId = UUID.randomUUID().toString()
        val testFuture = testRunnerThreadPool.submit {
            val testMethodsName = testMethods.map { it.method.name }.toSet()
            testMethods.forEach {
                if (Thread.interrupted()) {
                    removeSession(sessionId)
                    logger.info("Session: $sessionId was stopped")
                    Thread.currentThread().interrupt()
                    return@forEach
                }

                updateProgress(sessionId, it.method.name, testMethodsName)

                try {
                    invokeMethod(it, runPolicy)
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    return@forEach
                }
            }

            logger.info("Session completed $sessionId")
            removeSession(sessionId)
        }

        testFutureBySessionId[sessionId] = testFuture
        logger.info("Started to run tests sessionId: $sessionId, run policy: $runPolicy")
        return sessionId
    }

    override fun stop(sessionId: String) {
        val testsFuture = testFutureBySessionId[sessionId]
                ?: throw IllegalArgumentException("Session with id: $sessionId does not exist")
        val cancelStatus = testsFuture.cancel(true)
        logger.info("Test session: $sessionId, was cancelled $cancelStatus")
    }

    override fun getTestSessions(): List<TestSessionsDto> {
        return testSessionInformationBySessionId.values
                .map { Transformer.toTestSessionDto(it) }
    }

    private fun removeSession(sessionId: String) {
        testFutureBySessionId.remove(sessionId)
        testSessionInformationBySessionId.remove(sessionId)
    }

    private fun invokeMethod(method: TestMethodEntity, runPolicy: RunTestsPolicy) {
        val factory = applicationContext.autowireCapableBeanFactory
        val testBean = factory.createBean(method.method.declaringClass)

        runTestStrategyByRunTestsPolicy[runPolicy]?.invoke {
            try {
                IntRange(1, method.runCount).forEach { method.method.invoke(testBean) }
            } catch (e: InvocationTargetException) {
                throw e.cause ?: e
            }
        }
    }

    private fun updateProgress(sessionId: String,
                               currentlyRunning: String,
                               allTestNames: Set<String>) {
        val testSession = testSessionInformationBySessionId.getOrPut(sessionId) {
            TestSessionEntity(sessionId, 0.0, HashSet(), HashSet(allTestNames), null)
        }

        testSession.currentlyRunningTest?.let {
            testSession.testsAlreadyRunned.add(it)
        }

        testSession.progress = 100 * (testSession.testsAlreadyRunned.size * 1.0 / allTestNames.size)
        testSession.currentlyRunningTest = currentlyRunning
        testSession.testsToBeRun.remove(currentlyRunning)
    }
}