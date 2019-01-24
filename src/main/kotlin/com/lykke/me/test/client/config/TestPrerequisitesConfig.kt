package com.lykke.me.test.client.config

data class TestPrerequisitesConfig(val clientsConfig: ClientsConfig,
                                   val assetsConfig: Set<AssetPairConfig>,
                                   val maxOrdersInOrderBook: Int) {
    class ClientsConfig(val trustedClients: Set<String>, clients: Set<String>)
    class AssetPairConfig(val assetPairId: String, val baseAssetId: String, val quotingAssetId: String)
}