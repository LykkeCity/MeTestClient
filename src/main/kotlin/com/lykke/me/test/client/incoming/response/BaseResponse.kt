package com.lykke.me.test.client.incoming.response

class BaseResponse(val requestId: String,
                   override val messageId: String,
                   val matchingEngineId: String?,
                   val status: MessageStatus,
                   val statusReason: String?): Response