package com.test_crypto.domain.home.entities

data class DataWatchList(
    var watchLists: List<CurrencyDetails> = mutableListOf(),
)