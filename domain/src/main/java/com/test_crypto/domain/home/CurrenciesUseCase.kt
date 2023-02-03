package com.test_crypto.domain.home

import com.test_crypto.domain.DataState
import com.test_crypto.domain.core.FlowUseCase
import com.test_crypto.domain.home.entities.Currency
import com.test_crypto.domain.home.entities.DataCurrency
import com.test_crypto.domain.home.entities.FilterId
import com.test_crypto.domain.home.entities.toFeedEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrenciesUseCase @Inject constructor(
    private val repository: RepositoryCurrencies,
) : FlowUseCase<DataCurrency, GetCurrenciesUseCase.Params>() {

    override suspend fun invoke(params: Params): Flow<DataState<DataCurrency>> {
        return userCurrenciesData(params)
    }

    private suspend fun userCurrenciesData(params: Params): Flow<DataState<DataCurrency>> {
        println("userCurrenciesData params $params")

        val currencySource = when (params.filterId.id) {
            FilterId.ALL_ASSETS -> {
                repository.getCurrencies()
            }
            FilterId.WATCHLIST -> {
                repository.getCurrenciesWatchlist()
            }
            else -> repository.getCurrencies()
        }

        return flow {
            emit(fillData(currencySource.first()))
        }
    }


    private fun fillData(
        currencies: List<Currency>
    ): DataState<DataCurrency> {
        val dataCurrency = DataCurrency()
        dataCurrency.currencies = currencies.map {
            it.toFeedEntity()
        }
        return DataState.Success(dataCurrency)
    }

    data class Params(val filterId: FilterId)
}