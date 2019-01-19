package com.lykke.me.test.client.outgoing.messages.builders.common

import com.lykke.me.test.client.outgoing.messages.common.FeeSizeType
import com.lykke.me.test.client.outgoing.messages.common.FeeType
import com.lykke.me.test.client.outgoing.messages.common.SimpleFeeInstruction
import java.math.BigDecimal

class SimpleFeeInstructionBuilder {

    var type: FeeType? = null
    var sizeType: FeeSizeType? = null
    var size: BigDecimal? = null
    var sourceClientId: String? = null
    var targetClientId: String? = null
    var assetIds: Collection<String> = emptyList()

    fun build(): SimpleFeeInstruction {
        return SimpleFeeInstruction(type!!,
                sizeType,
                size,
                sourceClientId,
                targetClientId,
                assetIds)
    }
}