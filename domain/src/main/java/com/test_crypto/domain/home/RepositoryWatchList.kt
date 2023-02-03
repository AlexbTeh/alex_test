package com.test_crypto.domain.home

import com.test_crypto.domain.Empty
import com.test_crypto.domain.home.entities.CurrencyDetails
import kotlinx.coroutines.flow.Flow

interface RepositoryWatchList {
    suspend fun getWatchLists(): Flow<List<CurrencyDetails>>
    suspend fun addToWatchList(id: Int, currencyDetails: CurrencyDetails): Empty
    suspend fun deleteFromWatchList(id: Int): Empty
}