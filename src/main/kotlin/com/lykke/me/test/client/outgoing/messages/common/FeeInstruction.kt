package com.lykke.me.test.client.outgoing.messages.common

import java.math.BigDecimal

class FeeInstruction(val type: FeeType,
                     val takerSize: BigDecimal?,
                     val takerSizeType: FeeSizeType?,
                     val makerSize: BigDecimal?,
                     val makerSizeType: FeeSizeType?,
                     val sourceClientId: String?,
                     val targetClientId: String?,
                     val assetIds: Collection<String>)