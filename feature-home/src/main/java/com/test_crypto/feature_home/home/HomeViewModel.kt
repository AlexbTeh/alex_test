package com.test_crypto.feature_home.home

import com.test_crypto.core_ui.mvi.BaseViewModel
import com.test_crypto.core_ui.mvi.Reducer
import com.test_crypto.feature_home.home.mvi.HomeIntent
import com.test_crypto.feature_home.home.mvi.HomeReducer
import com.test_crypto.feature_home.home.mvi.HomeResult
import com.test_crypto.feature_home.home.mvi.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() :
    BaseViewModel<HomeState, HomeIntent, HomeResult, Reducer<HomeResult, HomeState>>() {

    override val state: Flow<HomeState>
        get() = reducer.state

    override fun dispatch(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.HomeInit -> {}
        }
    }

    override val reducer: Reducer<HomeResult, HomeState> = HomeReducer()
}