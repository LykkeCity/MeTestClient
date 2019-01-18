package com.lykke.me.test.client.service.impl

import com.lykke.me.test.client.service.TestsRunnerService
import com.lykke.me.test.client.service.RunTestsPolicy
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
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

    @Autowired
    private lateinit var testRunnerThreadPool: ThreadPoolTaskExecutor

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    override fun run(testMethods: List<Method>, runPolicy: RunTestsPolicy): String {
        val sessionId = UUID.randomUUID().toString()
        val testFuture = testRunnerThreadPool.submit {
            testMethods.forEach {
                if (Thread.interrupted()) {
                    testFutureBySessionId.remove(sessionId)
                    logger.info("Session: $sessionId was stopped")
                    Thread.currentThread().interrupt()
                    return@forEach
                }

                try {
                    invokeMethod(it, runPolicy)
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    return@forEach
                }
            }
            testFutureBySessionId.remove(sessionId)
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

    override fun getAllTestSessionIds(): Set<String> {
        return testFutureBySessionId.keys
    }

    private fun invokeMethod(method: Method, runPolicy: RunTestsPolicy) {
        val factory = applicationContext.autowireCapableBeanFactory
        val testBean = factory.createBean(method.declaringClass)

        runTestStrategyByRunTestsPolicy[runPolicy]?.invoke {
            try {
                method.invoke(testBean)
            } catch (e: InvocationTargetException) {
                throw e.cause ?: e
            }
        }
    }
}