package com.test_crypto.feature_home.watchlist.mvi

import com.test_crypto.core_ui.mvi.Reducer

class WatchListReducer : Reducer<WatchListResult, WatchListState>(WatchListState()) {
    override suspend fun reduce(result: WatchListResult, oldState: WatchListState) {
        when (result) {
            is WatchListResult.Error -> setState(
                oldState.copy(
                    error = result.exception,
                    loading = false,
                    isShowEmptyPage = false
                )
            )
            is WatchListResult.Success -> setState(
                oldState.copy(
                    error = null,
                    loading = false,
                    isShowEmptyPage = false
                )
            )
            is WatchListResult.Loading -> setState(oldState.copy(error = null, loading = true, isShowEmptyPage = false))

            is WatchListResult.Shimmer -> setState(
                oldState.copy(
                    error = null,
                    loading = true,
                    items = result.items,
                    isShowEmptyPage = false
                )
            )

            is WatchListResult.Items -> setState(
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