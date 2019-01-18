package com.lykke.me.test.client.service.impl

import com.lykke.me.test.client.service.TestsRunnerService
import com.lykke.me.test.client.service.RunTestsPolicy
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Component
class TestsRunnerServiceImpl : TestsRunnerService {

    companion object {
        private val logger = Logger.getLogger(TestsRunnerServiceImpl::class.java)
    }

    private val runTestStrategyByRunTestsPolicy = mapOf(RunTestsPolicy.CONTINUE_ON_ERROR to { test: () -> Unit ->
        try {
            test.invoke()
        } catch (e: Exception) {
            logger.error("Error running test", e)
        }
    },
            RunTestsPolicy.STOP_ON_ERROR to { test: () -> Unit ->
                {
                    test.invoke()
                }
            })

    @Autowired
    private lateinit var testRunnerThreadPool: ThreadPoolTaskExecutor

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    override fun run(testMethods: List<Method>, runPolicy: RunTestsPolicy) {
        testRunnerThreadPool.execute {
            testMethods.forEach { invokeMethod(it, runPolicy) }
        }
    }

    private fun invokeMethod(method: Method, runPolicy: RunTestsPolicy) {
        val factory = applicationContext.autowireCapableBeanFactory
        val testBean = factory.createBean(method.declaringClass)
        runTestStrategyByRunTestsPolicy[runPolicy]?.invoke { method.invoke(testBean) }
    }
}