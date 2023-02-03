package com.test_crypto.feature_home.prices.mvi

import com.test_crypto.core_ui.mvi.Reducer

class PricesReducer : Reducer<PricesResult, PricesState>(PricesState()) {
    override suspend fun reduce(result: PricesResult, oldState: PricesState) {
        when (result) {
            is PricesResult.Error -> setState(
                oldState.copy(
                    error = result.exception,
                    loading = false,
                    isShowEmptyPage = false
                )
            )
            is PricesResult.Success -> setState(
                oldState.copy(
                    error = null,
                    loading = false,
                    isShowEmptyPage = false
                )
            )
            is PricesResult.Loading -> setState(oldState.copy(error = null, loading = true, isShowEmptyPage = false))

            is PricesResult.Shimmer -> setState(
                oldState.copy(
                    error = null,
                    loading = true,
                    items = result.items,
                    isShowEmptyPage = false
                )
            )

            is PricesResult.Items -> setState(
                oldState.copy(
                    error = null,
                    loading = false,
                    items = result.items,
                    isShowEmptyPage = result.showEmptyPage
                )
            )
        }
    }
}