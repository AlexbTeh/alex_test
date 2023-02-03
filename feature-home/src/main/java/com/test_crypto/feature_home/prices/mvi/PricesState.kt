package com.test_crypto.feature_home.prices.mvi

import com.test_crypto.core_ui.adapter.DelegateAdapterItem
import com.test_crypto.core_ui.mvi.UiState
import com.test_crypto.domain.Failure

data class PricesState(
    val success: Boolean = false,
    val loading: Boolean = false,
    val error: Failure? = null,
    val items: List<DelegateAdapterItem>? = null,
    val isShowEmptyPage: Boolean = false,
) : UiState