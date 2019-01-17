package com.lykke.me.test.client.outgoing.messages.common

import java.math.BigDecimal

class SimpleFeeInstruction(val type: FeeType,
                           val sizeType: FeeSizeType?,
                           val size: BigDecimal?,
                           val sourceClientId: String?,
                           val targetClientId: String?,
                           val assetIds: Collection<String>)