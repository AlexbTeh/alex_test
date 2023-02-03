package com.test_crypto.domain.home.entities

data class DataCurrency(
    var currencies: List<FeedCurrency> = mutableListOf(),
)