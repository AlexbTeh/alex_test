package com.test_crypto.domain.home

import com.test_crypto.domain.Empty
import com.test_crypto.domain.core.UseCase
import com.test_crypto.domain.home.entities.CurrencyDetails
import javax.inject.Inject

class AddOrDeleteWatchListFlowUseCase @Inject constructor(
    private val repository: RepositoryWatchList,
) : UseCase<Empty, AddOrDeleteWatchListFlowUseCase.Params>() {

    override suspend fun invoke(params: Params): Empty {
        return if (params.isAdd) {
            repository.addToWatchList(params.id, params.currencyDetails)
        } else {
            repository.deleteFromWatchList(params.id)
        }
    }

    data class Params(val id: Int, val isAdd: Boolean, val currencyDetails: CurrencyDetails)
}