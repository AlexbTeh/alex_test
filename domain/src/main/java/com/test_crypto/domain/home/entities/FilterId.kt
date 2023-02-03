package com.test_crypto.domain.home.entities

data class FilterId(var id: Int) {
    companion object {
        const val ALL_ASSETS = -1
        const val WATCHLIST = -2
        const val TOP_GAINERS = -3
        const val TOP_LOSERS = -4
    }
}