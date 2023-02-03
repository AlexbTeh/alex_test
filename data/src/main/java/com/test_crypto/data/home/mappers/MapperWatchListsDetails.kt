package com.test_crypto.data.home.mappers

import com.test_crypto.data.commons.MapperSync
import com.test_crypto.data.home.dto.CurrencyDetailsDTO
import com.test_crypto.data.local_db.WatchListDB
import com.test_crypto.domain.home.entities.CurrencyDetails

class MapperWatchListsDetails : MapperSync<List<CurrencyDetailsDTO>, List<CurrencyDetails>, List<WatchListDB>> {
    override fun map(data: List<CurrencyDetailsDTO>): List<CurrencyDetails> {
        return data.map {
            MapperWatchListDetails().map(it)
        }
    }

    override fun mapToDb(data: List<CurrencyDetailsDTO>): List<WatchListDB> {
        return data.map {
            MapperWatchListDetails().mapToDb(it)
        }
    }

    override fun mapDbToEntity(data: List<WatchListDB>?): List<CurrencyDetails> {
        return data?.mapNotNull{
            MapperWatchListDetails().mapDbToEntity(it)
        } ?: mutableListOf()
    }
}