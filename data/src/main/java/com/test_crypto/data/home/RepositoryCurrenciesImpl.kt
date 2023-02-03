package com.test_crypto.data.home

import com.test_crypto.data.home.mappers.MapperCurrencies
import com.test_crypto.data.home.mappers.MapperCurrency
import com.test_crypto.data.home.mappers.MapperCurrencyDetails
import com.test_crypto.data.home.network.HomeDataSource
import com.test_crypto.data.local_db.daos.CurrencyDao
import com.test_crypto.domain.Empty
import com.test_crypto.domain.home.RepositoryCurrencies
import com.test_crypto.domain.home.entities.Currency
import com.test_crypto.domain.home.entities.CurrencyDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class RepositoryCurrenciesImpl @Inject constructor(
    private val remoteSource: HomeDataSource,
    private val localSource: CurrencyDao,
) : RepositoryCurrencies {
    private val mapperCurrencies = MapperCurrencies()
    private val mapperCurrency = MapperCurrency()
    private val mapperCurrencyDetails = MapperCurrencyDetails()

    override suspend fun getCurrencies(): Flow<List<Currency>> {
        return localSource.getCurrencies()
            .map { mapperCurrencies.mapDbToEntity(it) }
    }

    override suspend fun getCurrencyRemote(id: Int): CurrencyDetails {
        val currency = remoteSource.getCurrency(id)
        val currencyDetails = currency.data[id.toString()]
        return mapperCurrencyDetails.map(currencyDetails)
    }

    override suspend fun getCurrenciesWatchlist(): Flow<List<Currency>> {
        return localSource.getCurrenciesWatchlist()
            .map { mapperCurrencies.mapDbToEntity(it) }
    }

    override suspend fun getCurrencyLocal(code: String): Flow<Currency> {
        return localSource.getCurrency(code).mapNotNull { mapperCurrency.mapDbToEntity(it) }
    }

    override suspend fun syncCurrencies() {
        val start = 1
        val currencies = remoteSource.getCurrencies(start)
        localSource.reInsertCurrencies(mapperCurrencies.mapToDb(currencies.data))
    }
}