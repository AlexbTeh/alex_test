package com.test_crypto.domain.core

import android.util.Log
import com.test_crypto.domain.DataState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

abstract class NoParamsUse<Type> : BaseUseCase<Type>() {
    abstract suspend operator fun invoke() : Type
}

fun <Type> NoParamsUse<Type>.runCase(
    scope: CoroutineScope,
    emitLoading: Boolean = true,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    result: suspend (DataState<Type>) -> Unit = {}
) {
    scope.launch {
        withContext(dispatcher) {
            flow<DataState<Type>> {
                emit(DataState.Success(invoke()))
            }.emitStates(emitLoading).collectLatest {
                result.invoke(it)
            }
        }
    }
}

fun <Type> NoParamsUse<Type>.launchOn(
    scope: CoroutineScope,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    action: suspend (Type) -> Unit = {}
) {
    scope.launch(dispatcher) {
        try {
            action(invoke())
        } catch (ex: Exception) {
            Log.e("UseCase", ex.message.toString())
        }

    }
}