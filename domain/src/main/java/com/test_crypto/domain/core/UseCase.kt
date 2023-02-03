package com.test_crypto.domain.core

import android.util.Log
import com.test_crypto.domain.DataState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

abstract class UseCase<Type, in Params> : BaseUseCase<Type>() {
    abstract suspend operator fun invoke(params: Params): Type
}

fun <Type, Params> UseCase<Type, Params>.runCase(
    scope: CoroutineScope,
    params: Params,
    emitLoading: Boolean = true,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    result: (DataState<Type>) -> Unit
): Job {
    return scope.launch {
        withContext(dispatcher) {
            flow<DataState<Type>> {
                emit(DataState.Success(invoke(params)))
            }.emitStates(emitLoading, dispatcher).collectLatest {
                result.invoke(it)
            }
        }
    }
}

fun <Type, Params> UseCase<Type, Params>.launchOn(
    scope: CoroutineScope,
    params: Params,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    onSuccess: suspend (Type) -> Unit = {}
): Job {
    return scope.launch(dispatcher) {
        try {
            onSuccess(invoke(params))
        } catch (ex: Exception) {
            Log.e("UseCase", ex.message.toString())
        }
    }
}


