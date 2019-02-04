package com.lykke.me.test.client.service

import com.lykke.me.test.client.web.dto.AvailableTestsDto
import com.lykke.me.test.client.web.dto.TestSessionsDto

interface TestsService {
    fun startAllTests(runTestsPolicy: RunTestsPolicy?, messageRatePolicy: MessageRatePolicy?, messageDelayMs: Long?): String?

    fun startTestsByGroups(groupNames: Set<String>,
                           runTestsPolicy: RunTestsPolicy?,
                           messageRatePolicy: MessageRatePolicy?,
                           messageDelayMs: Long?): String?

    fun startTestsByNames(testNames: Set<String>,
                          runTestsPolicy: RunTestsPolicy?,
                          messageRatePolicy: MessageRatePolicy?,
                          messageDelayMs: Long?): String?

    fun getAllTests(): AvailableTestsDto

    fun getTestSessions(): List<TestSessionsDto>

    fun stopTestSession(sessionId: String)

    fun stopAllTestSessions()
}