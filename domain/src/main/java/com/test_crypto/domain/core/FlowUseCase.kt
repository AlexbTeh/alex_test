package com.test_crypto.domain.core

import com.test_crypto.domain.DataState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

abstract class FlowUseCase<Type, in Params> : BaseUseCase<Type>() {
    abstract suspend operator fun invoke(params: Params): Flow<DataState<Type>>
}

fun <Type, Params> FlowUseCase<Type, Params>.collect(
    scope: CoroutineScope,
    params: Params,
    emitLoading: Boolean = true,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    result: (DataState<Type>) -> Unit
) : Job {
    return scope.launch {
        withContext(dispatcher) {
            invoke(params)
                .emitStates(emitLoading, dispatcher)
                .collectLatest {
                    result.invoke(it)
                }
        }
    }
}

