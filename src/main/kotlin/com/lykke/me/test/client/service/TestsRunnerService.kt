package com.lykke.me.test.client.service

import com.lykke.me.test.client.web.dto.TestSessionsDto
import java.lang.reflect.Method

interface TestsRunnerService {
    /**
     * Runs test methods - return unique session id
     */
    fun run(testMethods: List<Method>, runPolicy: RunTestsPolicy = RunTestsPolicy.CONTINUE_ON_ERROR): String

    fun stop(sessionId: String)

    fun getTestSessions(): List<TestSessionsDto>
}