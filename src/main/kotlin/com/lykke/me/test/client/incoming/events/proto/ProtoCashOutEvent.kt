package com.lykke.me.test.client.incoming.events.proto

import com.lykke.me.test.client.incoming.events.CashOutEvent

class ProtoCashOutEvent(message: ProtocolEvents.CashOutEvent,
                        override val messageId: String) :
        MeProtoEvent<ProtocolEvents.CashOutEvent>(message), CashOutEvent