package com.test_crypto.domain.home.entities

data class FeedCurrency(
    var id: Int,
    var name: String? = null,
    var symbol: String? = null,
    var iconUrl: String? = null,
    var price: String? = null,
    var code: String? = null,
)