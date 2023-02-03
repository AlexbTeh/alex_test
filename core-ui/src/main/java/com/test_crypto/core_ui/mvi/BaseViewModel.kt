package com.test_crypto.core_ui.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

abstract class BaseViewModel<STATE : UiState, INTENT : UiIntent, RESULT : UiIntentResult, REDUCER : Reducer<RESULT, STATE>> :
    ViewModel() {
    abstract val state: Flow<STATE>

    abstract fun dispatch(intent : INTENT)

    abstract val reducer : REDUCER

}