package com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers

import com.google.protobuf.GeneratedMessageV3
import com.lykke.me.test.client.outgoing.messages.common.MessageType

class ProtoMessageWrapper(val generatedMessage: GeneratedMessageV3,
                          val messageType: MessageType)