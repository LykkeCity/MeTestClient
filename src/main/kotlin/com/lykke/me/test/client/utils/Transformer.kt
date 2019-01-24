package com.lykke.me.test.client.utils

import com.lykke.me.test.client.entity.TestSessionEntity
import com.lykke.me.test.client.web.dto.TestSessionsDto
import java.math.BigDecimal
import java.math.BigDecimal.ROUND_HALF_UP

class Transformer {
    companion object {
        fun toTestSessionDto(testSessionEntity: TestSessionEntity): TestSessionsDto {
            return testSessionEntity.let {
                TestSessionsDto(
                        it.sessionId,
                        BigDecimal.valueOf(it.progress).setScale(2, ROUND_HALF_UP).toDouble(),
                        it.testsAlreadyRunned,
                        it.testsToBeRun,
                        it.currentlyRunningTest!!)
            }
        }
    }
}