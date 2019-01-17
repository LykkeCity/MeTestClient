package com.lykke.me.test.client.outgoing.messages.common

enum class TimeInForce(val externalId: Int) {
    GTC(0),
    GTD(1),
    IOC(2),
    FOK(3);
}