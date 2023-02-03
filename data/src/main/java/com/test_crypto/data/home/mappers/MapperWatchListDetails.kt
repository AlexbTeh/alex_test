package com.test_crypto.data.home.mappers

import com.test_crypto.data.commons.MapperSync
import com.test_crypto.data.home.dto.CurrencyDetailsDTO
import com.test_crypto.data.local_db.WatchListDB
import com.test_crypto.domain.home.entities.CurrencyDetails

class MapperWatchListDetails : MapperSync<CurrencyDetailsDTO, CurrencyDetails, WatchListDB> {
    override fun map(data: CurrencyDetailsDTO): CurrencyDetails {
        return CurrencyDetails(
            id = data.id,
            description = data.description,
            name = data.name,
            symbol = data.symbol,
            iconUrl = data.iconUrl,
        )
    }

    override fun mapToDb(data: CurrencyDetailsDTO): WatchListDB {
        return   WatchListDB(
            id = data.id,
            description = data.description,
            name = data.name,
            symbol = data.symbol,
            iconUrl = data.iconUrl,
        )
    }

    override fun mapDbToEntity(data: WatchListDB?): CurrencyDetails? {
        return data?.let {
            CurrencyDetails(
                id = data.id,
                description = data.description,
                name = data.name,
                symbol = data.symbol,
                iconUrl = data.iconUrl,
            )
        }
    }
}