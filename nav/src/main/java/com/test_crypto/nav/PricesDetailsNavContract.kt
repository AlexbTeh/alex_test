package com.test_crypto.nav

import androidx.navigation.NavDeepLinkRequest

interface PricesDetailsNavContract {
    fun openCurrencyDetails(id: Int) : NavDeepLinkRequest
}