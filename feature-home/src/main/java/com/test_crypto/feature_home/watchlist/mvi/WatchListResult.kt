package com.test_crypto.feature_home.watchlist.mvi

import com.test_crypto.core_ui.adapter.DelegateAdapterItem
import com.test_crypto.core_ui.mvi.UiIntentResult
import com.test_crypto.domain.DataState
import com.test_crypto.domain.Failure
import com.test_crypto.domain.home.CurrenciesResponse
import com.test_crypto.domain.home.entities.DataCurrency
import com.test_crypto.domain.home.entities.DataWatchList

sealed class WatchListResult : UiIntentResult {
    object Success : WatchListResult()
    data class Error(val exception: Failure) : WatchListResult()
    data class Items(val items : List<DelegateAdapterItem>? = null, val showEmptyPage: Boolean) : WatchListResult()
    data class Data(val data : DataWatchList? = null) : WatchListResult()
    data class Shimmer(val items : List<DelegateAdapterItem>? = null) : WatchListResult()
    object Loading: WatchListResult()
}


fun DataState<DataWatchList>.toItems(): WatchListResult {
    return when (this) {
        is DataState.Error -> WatchListResult.Error(error)
        is DataState.Loading -> WatchListResult.Loading
        is DataState.Success -> WatchListResult.Data(this.data)
    }
}