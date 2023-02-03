package com.test_crypto.feature_home.watchlist.mvi

import com.test_crypto.core_ui.mvi.UiIntent
import com.test_crypto.domain.home.entities.DataWatchList

sealed class WatchListIntent : UiIntent {
    object InitialLoad : WatchListIntent()
    data class LoadItems(var data: DataWatchList) : WatchListIntent()
}