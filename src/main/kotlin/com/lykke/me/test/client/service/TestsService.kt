package com.lykke.me.test.client.service

interface TestsService {
    fun startAllTests(runTestsPolicy: RunTestsPolicy = RunTestsPolicy.CONTINUE_ON_ERROR)

    fun startTests(testNames: Set<String>, runTestsPolicy: RunTestsPolicy = RunTestsPolicy.CONTINUE_ON_ERROR)

    fun getTestNames(): Set<String>

    fun getTestSessionIds(): Set<String>

    fun stopTestSession(sessionId: String)
}