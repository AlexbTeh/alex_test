package com.test_crypto.core_ui.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class Reducer<in E : UiIntentResult, S : UiState>(initial: S) {
    private val _state: MutableStateFlow<S> = MutableStateFlow(initial)
    val state: StateFlow<S>
        get() = _state

    abstract suspend fun reduce(result: E, oldState: S)

    fun sendEvent(event: E) {
        CoroutineScope(Dispatchers.IO).launch {
            reduce(event, state.value)
        }
    }

    protected fun setState(newState: S) {
        val success = _state.tryEmit(newState)
        println("Reducer  setState success $success")
        println("Reducer  setState newState $newState")
    }

}
