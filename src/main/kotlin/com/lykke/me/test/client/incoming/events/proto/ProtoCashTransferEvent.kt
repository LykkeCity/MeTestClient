package com.lykke.me.test.client.incoming.events.proto

import com.lykke.me.test.client.incoming.events.CashTransferEvent

class ProtoCashTransferEvent(message: ProtocolEvents.CashTransferEvent,
                             override val messageId: String) :
        MeProtoEvent<ProtocolEvents.CashTransferEvent>(message), CashTransferEvent