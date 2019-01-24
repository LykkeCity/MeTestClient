package com.lykke.me.test.client.incoming.events.proto

import com.google.protobuf.GeneratedMessageV3
import com.lykke.me.test.client.incoming.events.MeEvent

abstract class MeProtoEvent<T: GeneratedMessageV3>(val message: T): MeEvent