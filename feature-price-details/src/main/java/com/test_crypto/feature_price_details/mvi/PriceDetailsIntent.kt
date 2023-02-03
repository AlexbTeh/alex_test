package com.test_crypto.feature_price_details.mvi

import com.test_crypto.core_ui.mvi.UiIntent

sealed class PriceDetailsIntent: UiIntent {
    data class LoadData(var id: Int) : PriceDetailsIntent()
    object LoadItems : PriceDetailsIntent()
    data class AddWatchList(var id: Int, var isAdd : Boolean): PriceDetailsIntent()
}