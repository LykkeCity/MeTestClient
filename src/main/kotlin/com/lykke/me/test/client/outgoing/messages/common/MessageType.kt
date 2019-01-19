package com.lykke.me.test.client.outgoing.messages.common

import java.lang.IllegalArgumentException

enum class MessageType(val type: Byte) {
    RESPONSE(0),
    PING(1),
    CASH_TRANSFER_OPERATION(8),
    CASH_IN_OUT_OPERATION(9),
    ORDER_BOOK_SNAPSHOT(40),
    LIMIT_ORDER(50),
    MULTI_LIMIT_ORDER(51),
    MARKET_ORDER(53),
    LIMIT_ORDER_CANCEL(55),
    MULTI_LIMIT_ORDER_CANCEL(57),
    NEW_RESPONSE(99),
    MARKET_ORDER_RESPONSE(100),
    MULTI_LIMIT_ORDER_RESPONSE(98),
    RESERVED_CASH_IN_OUT_OPERATION(120),
    LIMIT_ORDER_MASS_CANCEL(121)
    ;

    companion object {
        private val valuesByType = MessageType.values()
                .groupBy { it.type }
                .mapValues { it.value.single() }

        fun getByType(type: Byte): MessageType {
            return valuesByType[type]
                    ?: throw IllegalArgumentException("Unknown message type: $type")
        }
    }
}