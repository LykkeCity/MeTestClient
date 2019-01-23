package com.lykke.me.test.client.incoming.events.proto

import com.lykke.me.test.client.incoming.events.CashInEvent

class ProtoCashInEvent(message: ProtocolEvents.CashInEvent,
                       override val messageId: String) :
        MeProtoEvent<ProtocolEvents.CashInEvent>(message), CashInEvent