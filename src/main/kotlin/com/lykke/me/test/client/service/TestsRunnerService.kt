package com.lykke.me.test.client.service

import java.lang.reflect.Method

interface TestsRunnerService {
    fun run(testMethods: List<Method>, runPolicy: RunTestsPolicy = RunTestsPolicy.CONTINUE_ON_ERROR)
}