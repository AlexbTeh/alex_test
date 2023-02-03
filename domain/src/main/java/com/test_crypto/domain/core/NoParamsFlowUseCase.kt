package com.test_crypto.domain.core

import com.test_crypto.domain.DataState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

abstract class NoParamsFlowUseCase<Type> : BaseUseCase<Type>() {
    abstract suspend operator fun invoke(): Flow<DataState<Type>>
}

fun <Type> NoParamsFlowUseCase<Type>.collect(
    scope: CoroutineScope,
    emitLoading: Boolean = true,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    result: (DataState<Type>) -> Unit
) : Job {
    return scope.launch {
        withContext(dispatcher) {
            invoke()
                .emitStates(emitLoading, dispatcher)
                .collectLatest {
                    result.invoke(it)
                }
        }
    }
}