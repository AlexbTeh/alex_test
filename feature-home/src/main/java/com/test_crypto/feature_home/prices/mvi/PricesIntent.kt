package com.test_crypto.feature_home.prices.mvi

import com.test_crypto.core_ui.mvi.UiIntent
import com.test_crypto.domain.home.entities.DataCurrency

sealed class PricesIntent : UiIntent {
    object RefreshIntent: PricesIntent()
    object ShowWatchListIntent: PricesIntent()
    object HideWatchlistIntent: PricesIntent()
    object InitialLoad : PricesIntent()
    data class SelectFilter(val id : Int) : PricesIntent()
    data class UnselectFilter(val id : Int) : PricesIntent()
    data class LoadItems(var data: DataCurrency) : PricesIntent()
}