package com.lykke.me.test.client.config

data class TestPrerequisitesConfig(val clientsConfig: ClientsConfig,
                                   val assetsConfig: Set<AssetPairConfig>,
                                   val maxOrdersInOrderBook: Int) {

    class ClientsConfig(val trustedClients: Set<String>, val clients: Set<String>)

    class AssetPairConfig(val assetPairId: String,
                          val assetPairAccuracy: Int,
                          val baseAssetId: String,
                          val baseAssetAccuracy: Int,
                          val quotingAssetId: String,
                          val quotingAssetAccuracy: Int)
}