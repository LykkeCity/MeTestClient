package com.lykke.me.test.client.service.impl

import com.lykke.me.test.client.service.MessageRatePolicy
import com.lykke.me.test.client.service.RunTestsPolicy
import com.lykke.me.test.client.service.TestsFinderService
import com.lykke.me.test.client.service.TestsRunnerService
import com.lykke.me.test.client.service.TestsService
import com.lykke.me.test.client.web.dto.AvailableTestsDto
import com.lykke.me.test.client.web.dto.TestGroup
import com.lykke.me.test.client.web.dto.TestSessionsDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TestsServiceImpl : TestsService {

    @Autowired
    private lateinit var testsFinder: TestsFinderService

    @Autowired
    private lateinit var testsRunnerService: TestsRunnerService

    override fun startAllTests(runTestsPolicy: RunTestsPolicy?,
                               messageRatePolicy: MessageRatePolicy?,
                               messageDelayMs: Long?): String? {
        val allTestMethods = testsFinder.getAllTestMethods()
        if (allTestMethods.isNotEmpty()) {
            return testsRunnerService.run(allTestMethods,
                    runTestsPolicy,
                    messageRatePolicy,
                    messageDelayMs)
        }

        return null
    }

    override fun startTestsByNames(testNames: Set<String>,
                                   runTestsPolicy: RunTestsPolicy?,
                                   messageRatePolicy: MessageRatePolicy?,
                                   messageDelayMs: Long?): String? {
        val testMethods = testsFinder.getTestMethodsByTestNames(testNames)
        if (testMethods.isNotEmpty()) {
            return testsRunnerService.run(testMethods,
                    runTestsPolicy,
                    messageRatePolicy,
                    messageDelayMs)
        }

        return null
    }

    override fun startTestsByGroups(groupNames: Set<String>,
                            runTestsPolicy: RunTestsPolicy?,
                            messageRatePolicy: MessageRatePolicy?,
                            messageDelayMs: Long?): String? {
        val testMethods = testsFinder.getTestMethodsByGroupNames(groupNames)
        if (testMethods.isNotEmpty()) {
            return testsRunnerService.run(testMethods,
                    runTestsPolicy,
                    messageRatePolicy,
                    messageDelayMs)
        }

        return null
    }

    override fun getAllTests(): AvailableTestsDto {
       val testGroups = ArrayList<TestGroup>()
        val methodNamesByGroup = testsFinder.getAllTestMethods()
                .groupBy {it.testGroup}
                .mapValues { it.value.map { it.method.name }.toSet() }

        methodNamesByGroup.forEach { key, value ->
            testGroups.add(TestGroup(key, value))
        }

        return AvailableTestsDto(testGroups)
    }

    override fun getTestSessions(): List<TestSessionsDto> {
        return testsRunnerService.getTestSessions()
    }

    override fun stopTestSession(sessionId: String) {
        testsRunnerService.stop(sessionId)
    }

    override fun stopAllTestSessions() {
        testsRunnerService.stopAll()
    }
}