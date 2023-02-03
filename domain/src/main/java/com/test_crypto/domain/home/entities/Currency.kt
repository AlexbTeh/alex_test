package com.test_crypto.domain.home.entities

data class Currency(
    var id: Int,
    var code: String? = null,
    var name: String? = null,
    var symbol: String? = null,
    var iconUrl: String? = null,
    var hex: String? = null,
    var price: String? = null,
    var ranking: Int? = null,
    var dailyVolume: String? = null,
    var circulatingSupply: String? = null,
    var marketCap: String? = null
)


fun Currency.calculateExchangeRate(): Double {
    return price?.toDouble() ?: 0.0
}


