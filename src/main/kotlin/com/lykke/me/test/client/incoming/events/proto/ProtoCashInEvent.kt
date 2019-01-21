package com.lykke.me.test.client.incoming.events.proto

import com.lykke.me.test.client.incoming.events.CashInEvent

class ProtoCashInEvent(message: ProtocolEvents.CashInEvent) :
        MeProtoEvent<ProtocolEvents.CashInEvent>(message), CashInEvent