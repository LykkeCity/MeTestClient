package com.lykke.me.test.client.outgoing.messages.common

enum class LimitOrderType(val externalId: Int) {
    LIMIT(0),
    STOP_LIMIT(1);
}