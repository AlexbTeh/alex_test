package com.test_crypto.feature_price_details.mvi

import com.test_crypto.core_ui.adapter.DelegateAdapterItem
import com.test_crypto.core_ui.mvi.UiIntentResult
import com.test_crypto.domain.DataState
import com.test_crypto.domain.Failure
import com.test_crypto.domain.home.entities.CurrencyDetails

sealed class PriceDetailsResult : UiIntentResult {
    object Success : PriceDetailsResult()
    data class Error(val exception: Failure) : PriceDetailsResult()
    data class Items(
        val items: List<DelegateAdapterItem>? = null,
        val isFavorite: Boolean = false,
        val dataCurrency: CurrencyDetails?
    ) : PriceDetailsResult()
    object Loading: PriceDetailsResult()
    object LoadingFavorite: PriceDetailsResult()
}

//Mappers
fun DataState<CurrencyDetails?>.toDataCurrencyDetails(): PriceDetailsResult {
    return when (this) {
        is DataState.Error -> PriceDetailsResult.Error(error)
        is DataState.Loading -> PriceDetailsResult.Loading
        is DataState.Success -> PriceDetailsResult.Success
    }
}

