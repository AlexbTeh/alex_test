package com.test_crypto.domain.home

import com.test_crypto.domain.DataState
import com.test_crypto.domain.core.FlowUseCase
import com.test_crypto.domain.home.entities.CurrencyDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrencyFlowUseCase @Inject constructor(
    private val repositoryCurrencies: RepositoryCurrencies,
    private val repositoryWatchList: RepositoryWatchList
) : FlowUseCase<CurrencyDetails, CurrencyFlowUseCase.Params>() {

    override suspend fun invoke(params: Params): Flow<DataState<CurrencyDetails>> {
        return currencyData(params)
    }


    private suspend fun currencyData(params: Params): Flow<DataState<CurrencyDetails>> {

        val sourceCurrencyDetails = flow {
            emit(repositoryCurrencies.getCurrencyRemote(params.id))
        }
        return combine(
            sourceCurrencyDetails,
            repositoryWatchList.getWatchLists(),
        ) { currencyDetails, watchLists ->
            val isFavorite = watchLists.find { it.id == params.id} != null
            fillData(currencyDetails, isFavorite)
        }
    }


    private fun fillData(
        currency: CurrencyDetails,
        isFavorite: Boolean,
    ): DataState<CurrencyDetails> {

        val currencyDetails = CurrencyDetails(
            id = currency.id,
            symbol = currency.symbol ?: "",
            iconUrl = currency.iconUrl,
            description = currency.description ?: "",
            name = currency.name,
            isFavorite = isFavorite

        )
        return DataState.Success(currencyDetails)
    }

    data class Params(val id: Int)
}

