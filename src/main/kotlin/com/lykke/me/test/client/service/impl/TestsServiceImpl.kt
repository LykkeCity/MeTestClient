package com.lykke.me.test.client.service.impl

import com.lykke.me.test.client.service.RunTestsPolicy
import com.lykke.me.test.client.service.TestsFinderService
import com.lykke.me.test.client.service.TestsRunnerService
import com.lykke.me.test.client.service.TestsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TestsServiceImpl: TestsService {

    @Autowired
    private lateinit var testsFinder: TestsFinderService

    @Autowired
    private lateinit var testsRunnerService: TestsRunnerService

    override fun startAllTests(runTestsPolicy: RunTestsPolicy) {
        val allTestMethods = testsFinder.getAllTestMethods()
        if (allTestMethods.isNotEmpty()) {
            testsRunnerService.run(allTestMethods, runTestsPolicy)
        }
    }

    override fun startTests(testNames: Set<String>, runTestsPolicy: RunTestsPolicy) {
        val testMethods = testsFinder.getTestMethods(testNames)
        if (testMethods.isNotEmpty()) {
            testsRunnerService.run(testMethods, runTestsPolicy)
        }
    }

    override fun getTestNames(): Set<String> {
        return testsFinder.getTestNames()
    }
}