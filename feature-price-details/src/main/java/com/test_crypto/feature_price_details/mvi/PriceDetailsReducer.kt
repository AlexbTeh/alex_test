package com.test_crypto.feature_price_details.mvi

import com.test_crypto.core_ui.mvi.Reducer

class PriceDetailsReducer : Reducer<PriceDetailsResult, PriceDetailsState>(PriceDetailsState()) {
    override suspend fun reduce(result: PriceDetailsResult, oldState: PriceDetailsState) {
        when (result) {
            is PriceDetailsResult.Error -> setState(
                oldState.copy(
                    error = result.exception,
                    loading = false,
                    dataCurrencyDetails = null,
                    loadingFavorite = false
                )
            )
            is PriceDetailsResult.Success -> setState(
                oldState.copy(
                    error = null,
                    loading = false,
                    loadingFavorite = false
                )
            )
            is PriceDetailsResult.Loading -> setState(oldState.copy(error = null, loading = true, loadingFavorite = false))
            is PriceDetailsResult.Items -> setState(
                oldState.copy(
                    error = null,
                    loading = false,
                    items = result.items,
                    isFavorite = result.isFavorite,
                    loadingFavorite = false,
                    dataCurrencyDetails = result.dataCurrency
                )
            )
            is PriceDetailsResult.LoadingFavorite -> setState(oldState.copy(error = null, loading = false, loadingFavorite = true))
        }
    }
}