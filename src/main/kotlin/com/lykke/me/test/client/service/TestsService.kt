package com.lykke.me.test.client.service

import com.lykke.me.test.client.web.dto.TestSessionsDto

interface TestsService {
    fun startAllTests(runTestsPolicy: RunTestsPolicy = RunTestsPolicy.CONTINUE_ON_ERROR)

    fun startTests(testNames: Set<String>, runTestsPolicy: RunTestsPolicy = RunTestsPolicy.CONTINUE_ON_ERROR)

    fun getTestNames(): Set<String>

    fun getTestSessions(): List<TestSessionsDto>

    fun stopTestSession(sessionId: String)
}