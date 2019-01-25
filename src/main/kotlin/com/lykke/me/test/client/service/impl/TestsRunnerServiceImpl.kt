package com.lykke.me.test.client.service.impl

import com.lykke.me.test.client.entity.TestSessionEntity
import com.lykke.me.test.client.service.MessageRatePolicy
import com.lykke.me.test.client.service.TestsRunnerService
import com.lykke.me.test.client.service.RunTestsPolicy
import com.lykke.me.test.client.socket.MeClientFactory
import com.lykke.me.test.client.utils.Transformer
import com.lykke.me.test.client.web.dto.TestSessionsDto
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils
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
        private const val ME_CLIENT_FIELD_NAME = "meClient"
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

    @Autowired
    private lateinit var meClientFactory: MeClientFactory

    override fun run(testMethods: List<Method>,
                     runTestsPolicy: RunTestsPolicy?,
                     messageRatePolicy: MessageRatePolicy?,
                     messageDelayMs: Long?): String {
        val runPolicy = runTestsPolicy ?: RunTestsPolicy.CONTINUE_ON_ERROR
        val messageRate = messageRatePolicy ?: MessageRatePolicy.AUTO_MESSAGE_RATE

        if (messageRatePolicy == MessageRatePolicy.MANUAL_MESSAGE_RATE && messageDelayMs == null) {
            throw IllegalArgumentException("Manual message rate mode requires 'messageDelayMs' parameter")
        }

        val sessionId = UUID.randomUUID().toString()
        val testFuture = testRunnerThreadPool.submit {
            val testMethodsName = testMethods.map { it.name }.toSet()
            testMethods.forEach {
                if (Thread.interrupted()) {
                    removeSession(sessionId)
                    logger.info("Session: $sessionId was stopped")
                    Thread.currentThread().interrupt()
                    return@forEach
                }
                updateProgress(sessionId, it.name, testMethodsName)

                try {
                    invokeMethod(it, runPolicy, messageRate, messageDelayMs)
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

    private fun invokeMethod(method: Method,
                             runPolicy: RunTestsPolicy,
                             messageRatePolicy: MessageRatePolicy,
                             messageDelayMs: Long?) {
        val factory = applicationContext.autowireCapableBeanFactory

        val testBean = factory.createBean(method.declaringClass)
        setClient(testBean, messageRatePolicy, messageDelayMs)

        runTestStrategyByRunTestsPolicy[runPolicy]?.invoke {
            try {
                method.invoke(testBean)
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

    private fun setClient(bean: Any, messageRatePolicy: MessageRatePolicy, messageDelayMs: Long?)  {
        val client = meClientFactory.getClient(messageRatePolicy, messageDelayMs)
        val clientField = ReflectionUtils.findField(bean::class.java, ME_CLIENT_FIELD_NAME)
        clientField!!.isAccessible = true
        ReflectionUtils.setField(clientField, bean, client)
    }
}