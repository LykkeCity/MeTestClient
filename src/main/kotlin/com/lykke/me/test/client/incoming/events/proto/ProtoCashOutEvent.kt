package com.lykke.me.test.client.incoming.events.proto

import com.lykke.me.test.client.incoming.events.CashOutEvent

class ProtoCashOutEvent(message: ProtocolEvents.CashOutEvent) :
        MeProtoEvent<ProtocolEvents.CashOutEvent>(message), CashOutEvent