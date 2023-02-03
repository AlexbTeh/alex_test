package com.test_crypto.domain.home

import com.test_crypto.domain.Empty
import com.test_crypto.domain.core.NoParamsUse
import javax.inject.Inject

class SyncCurrencies @Inject constructor(
    private val repository: RepositoryCurrencies,
) : NoParamsUse<Empty>() {
    override suspend fun invoke(): Empty {
        println("SyncCurrenciesAndUser FragmentPrices runCase initialLoad ")
        repository.syncCurrencies()
        return Empty
    }
}