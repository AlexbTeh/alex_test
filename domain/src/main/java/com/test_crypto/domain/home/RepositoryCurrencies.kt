package com.test_crypto.domain.home

import com.test_crypto.domain.home.entities.Currency
import com.test_crypto.domain.home.entities.CurrencyDetails
import kotlinx.coroutines.flow.Flow

interface RepositoryCurrencies {
    suspend fun getCurrencies(): Flow<List<Currency>>
    suspend fun getCurrencyLocal(code: String): Flow<Currency>
    suspend fun getCurrenciesWatchlist() : Flow<List<Currency>>
    suspend fun getCurrencyRemote(id: Int): CurrencyDetails
    suspend fun syncCurrencies()
}