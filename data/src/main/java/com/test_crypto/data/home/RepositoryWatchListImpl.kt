package com.test_crypto.data.home

import com.test_crypto.data.home.dto.CurrencyDetailsDTO
import com.test_crypto.data.home.mappers.MapperWatchListDetails
import com.test_crypto.data.home.mappers.MapperWatchListsDetails
import com.test_crypto.data.local_db.daos.WatchListDao
import com.test_crypto.domain.Empty
import com.test_crypto.domain.home.RepositoryWatchList
import com.test_crypto.domain.home.entities.CurrencyDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepositoryWatchListImpl @Inject constructor(
    private val localSource: WatchListDao,
) : RepositoryWatchList {

    private val mapperWatchListsDetails = MapperWatchListsDetails()
    private val mapperWatchListDetails = MapperWatchListDetails()

    override suspend fun getWatchLists(): Flow<List<CurrencyDetails>> {
        return localSource.getWatchLists()
            .map { mapperWatchListsDetails.mapDbToEntity(it) }
    }

    override suspend fun addToWatchList(
        id: Int,
        currencyDetails: CurrencyDetails
    ): Empty {
        localSource.insertWatchList(
            mapperWatchListDetails.mapToDb(
                CurrencyDetailsDTO(
                    id = currencyDetails.id,
                    name = currencyDetails.name,
                    description = currencyDetails.description,
                    symbol = currencyDetails.symbol,
                    iconUrl = currencyDetails.iconUrl
                )
            )
        )

        return Empty
    }

    override suspend fun deleteFromWatchList(id: Int): Empty {
        localSource.deleteById(id)
        return Empty
    }
}