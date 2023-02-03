package com.test_crypto.feature_price_details.mvi

import com.test_crypto.core_ui.adapter.DelegateAdapterItem
import com.test_crypto.core_ui.mvi.UiState
import com.test_crypto.domain.Failure
import com.test_crypto.domain.home.entities.CurrencyDetails

data class PriceDetailsState(
    val dataCurrencyDetails: CurrencyDetails? = null,
    val isFavorite: Boolean = false,
    val loading: Boolean = false,
    val error: Failure? = null,
    val items: List<DelegateAdapterItem>? = null,
    val loadingFavorite: Boolean = false,
) : UiState