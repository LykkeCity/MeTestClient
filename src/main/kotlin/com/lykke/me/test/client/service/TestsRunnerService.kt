package com.lykke.me.test.client.service

import com.lykke.me.test.client.entity.TestMethodEntity
import com.lykke.me.test.client.web.dto.TestSessionsDto

interface TestsRunnerService {
    /**
     * Runs test methods - return unique session id
     */
    fun run(testMethods: List<TestMethodEntity>,
            runPolicy: RunTestsPolicy = RunTestsPolicy.CONTINUE_ON_ERROR,
            messageRatePolicy: MessageRatePolicy?,
            messageDelayMs: Long?): String

    fun stop(sessionId: String)

    fun getTestSessions(): List<TestSessionsDto>
}