package com.lykke.me.test.client.outgoing.messages.common

enum class OrderCancelMode(val externalId: Int) {
    NOT_EMPTY_SIDE(0),
    BOTH_SIDES(1),
    SELL_SIDE(2),
    BUY_SIDE(3);
}