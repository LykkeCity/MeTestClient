package com.lykke.me.test.client.incoming.events.proto

import com.lykke.me.test.client.incoming.events.ExecutionEvent

class ProtoExecutionEvent(message: ProtocolEvents.ExecutionEvent,
                          override val messageId: String) :
        MeProtoEvent<ProtocolEvents.ExecutionEvent>(message), ExecutionEvent