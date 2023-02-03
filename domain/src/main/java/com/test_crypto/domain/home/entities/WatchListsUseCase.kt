package com.test_crypto.domain.home.entities

import com.test_crypto.domain.DataState
import com.test_crypto.domain.core.FlowUseCase
import com.test_crypto.domain.home.RepositoryWatchList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WatchListsUseCase @Inject constructor(
    private val repository: RepositoryWatchList,
) : FlowUseCase<DataWatchList, WatchListsUseCase.Params>() {

    override suspend fun invoke(params: Params): Flow<DataState<DataWatchList>> {
        return userCurrenciesData(params)
    }

    private suspend fun userCurrenciesData(params: Params): Flow<DataState<DataWatchList>> {

        val currencySource = repository.getWatchLists()

        return flow {
            emit(fillData(currencySource.first()))
        }
    }


    private fun fillData(
        watchLists: List<CurrencyDetails>
    ): DataState<DataWatchList> {
        val dataCurrency = DataWatchList()
        dataCurrency.watchLists = watchLists
        return DataState.Success(dataCurrency)
    }

    object Params
}