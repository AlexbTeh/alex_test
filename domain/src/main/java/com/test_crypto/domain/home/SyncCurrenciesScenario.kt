package com.test_crypto.domain.home

import com.test_crypto.domain.core.NoParamsUse
import javax.inject.Inject

class SyncCurrenciesScenario @Inject constructor(private val syncCurrenciesAndUser: SyncCurrencies) : NoParamsUse<CurrenciesResponse>() {
    override suspend fun invoke(): CurrenciesResponse {
        syncCurrenciesAndUser()
        return CurrenciesResponse
    }

}

object CurrenciesResponse