package com.test_crypto.feature_home.prices.mvi

import com.test_crypto.core_ui.adapter.DelegateAdapterItem
import com.test_crypto.core_ui.mvi.UiIntentResult
import com.test_crypto.domain.DataState
import com.test_crypto.domain.Failure
import com.test_crypto.domain.home.CurrenciesResponse
import com.test_crypto.domain.home.entities.DataCurrency

sealed class PricesResult : UiIntentResult {
    object Success : PricesResult()
    data class Error(val exception: Failure) : PricesResult()
    data class Items(val items : List<DelegateAdapterItem>? = null, val showEmptyPage: Boolean) : PricesResult()
    data class Data(val data : DataCurrency? = null) : PricesResult()
    data class Shimmer(val items : List<DelegateAdapterItem>? = null) : PricesResult()
    object Loading: PricesResult()
}


fun DataState<DataCurrency>.toItems(): PricesResult {
    return when (this) {
        is DataState.Error -> PricesResult.Error(error)
        is DataState.Loading -> PricesResult.Loading
        is DataState.Success -> PricesResult.Data(this.data)
    }
}

fun DataState<CurrenciesResponse>.toResult() : PricesResult {
    return when (this) {
        is DataState.Error -> PricesResult.Error(error)
        is DataState.Loading -> PricesResult.Loading
        is DataState.Success -> PricesResult.Data(null)
    }
}