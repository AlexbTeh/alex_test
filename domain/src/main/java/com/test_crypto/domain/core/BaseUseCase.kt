package com.test_crypto.domain.core

import com.test_crypto.domain.DataState
import com.test_crypto.domain.getRestError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

abstract class BaseUseCase<Type> {
    fun Flow<DataState<Type>>.emitStates(emitLoading: Boolean, dispatcher : CoroutineContext = Dispatchers.IO) : Flow<DataState<Type>> {
        return onStart {
            if (emitLoading)
                emit((DataState.Loading))
        }.catch {
            it.printStackTrace()
            emit(DataState.Error(it.getRestError<Error>()))
        }.flowOn(dispatcher)
    }
}