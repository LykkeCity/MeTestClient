package com.lykke.me.test.client.incoming.events.proto

import com.lykke.me.test.client.incoming.events.ReservedBalanceUpdateEvent

class ProtoReservedBalanceUpdateEvent(message: ProtocolEvents.ReservedBalanceUpdateEvent,
                                      override val messageId: String) :
        MeProtoEvent<ProtocolEvents.ReservedBalanceUpdateEvent>(message), ReservedBalanceUpdateEvent