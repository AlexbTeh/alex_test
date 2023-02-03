package com.test_crypto.domain.home.entities

data class CurrencyDetails(
    var id: Int = 0,
    var symbol: String? = null,
    var name: String? = null,
    var iconUrl: String? = null,
    var description: String? = null,
    var isFavorite: Boolean = false,
) {
}