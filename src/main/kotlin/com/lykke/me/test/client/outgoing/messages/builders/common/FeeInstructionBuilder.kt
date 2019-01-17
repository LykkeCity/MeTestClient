package com.lykke.me.test.client.outgoing.messages.builders.common

import com.lykke.me.test.client.outgoing.messages.common.FeeInstruction
import com.lykke.me.test.client.outgoing.messages.common.FeeSizeType
import com.lykke.me.test.client.outgoing.messages.common.FeeType
import java.math.BigDecimal

class FeeInstructionBuilder {

    var type: FeeType? = null
    var takerSize: BigDecimal? = null
    var takerSizeType: FeeSizeType? = null
    var makerSize: BigDecimal? = null
    var makerSizeType: FeeSizeType? = null
    var sourceClientId: String? = null
    var targetClientId: String? = null
    var assetIds: Collection<String> = emptyList()

    fun build(): FeeInstruction {
        return FeeInstruction(type!!,
                takerSize,
                takerSizeType,
                makerSize,
                makerSizeType,
                sourceClientId,
                targetClientId,
                assetIds)
    }
}