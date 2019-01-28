package com.lykke.me.test.client.service

import com.lykke.me.test.client.web.dto.TestSessionsDto

interface TestsService {
    fun startAllTests(runTestsPolicy: RunTestsPolicy?, messageRatePolicy: MessageRatePolicy?, messageDelayMs: Long?): String?

    fun startTests(testNames: Set<String>, runTestsPolicy: RunTestsPolicy?,  messageRatePolicy: MessageRatePolicy?, messageDelayMs: Long?): String?

    fun getTestNames(): Set<String>

    fun getTestSessions(): List<TestSessionsDto>

    fun stopTestSession(sessionId: String)
}