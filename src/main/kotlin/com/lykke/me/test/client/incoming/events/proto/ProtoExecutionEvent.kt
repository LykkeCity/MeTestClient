package com.lykke.me.test.client.incoming.events.proto

import com.lykke.me.test.client.incoming.events.ExecutionEvent

class ProtoExecutionEvent(message: ProtocolEvents.ExecutionEvent) :
        MeProtoEvent<ProtocolEvents.ExecutionEvent>(message), ExecutionEvent