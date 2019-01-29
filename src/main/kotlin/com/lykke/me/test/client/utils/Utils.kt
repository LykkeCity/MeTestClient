package com.lykke.me.test.client.utils

import com.lykke.me.test.client.outgoing.messages.Message
import java.math.BigDecimal

fun generateMessages(count: Int, strategy: (Int) -> Message): List<Message> {
    return IntRange(1, count).map { strategy.invoke(it) }
}

/**
 * Calculates arithmetic progression
 */
fun calculateFundsNeeded(opsCount: BigDecimal, startAmount: BigDecimal, opDelta: BigDecimal): BigDecimal {
    return (startAmount.plus(startAmount.plus(opDelta.multiply(opsCount))).divide(BigDecimal.valueOf(2))).multiply(opsCount)
}