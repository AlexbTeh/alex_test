package com.test_crypto.data.home.mappers

import com.test_crypto.data.commons.MapperSync
import com.test_crypto.data.home.dto.CurrencyDTO
import com.test_crypto.data.local_db.CurrencyDB
import com.test_crypto.domain.home.entities.Currency

class MapperCurrency : MapperSync<CurrencyDTO, Currency, CurrencyDB> {
    override fun map(data: CurrencyDTO): Currency {
        return Currency(
            id = data.id,
            code = data.code,
            name = data.name,
            symbol = data.symbol,
            iconUrl = data.iconUrl,
            price = data.price,
            ranking = data.ranking,
            dailyVolume = data.dailyVolume,
            circulatingSupply = data.circulatingSupply,
            marketCap = data.marketCap
        )
    }

    override fun mapToDb(data: CurrencyDTO): CurrencyDB {
        return CurrencyDB(
            id = data.id,
            code = data.code,
            name = data.name,
            symbol = data.symbol,
            iconUrl = data.iconUrl,
            price = data.price,
            ranking = data.ranking,
            dailyVolume = data.dailyVolume,
            circulatingSupply = data.circulatingSupply,
            marketCap = data.marketCap
        )
    }

    override fun mapDbToEntity(data: CurrencyDB?): Currency? {
        return data?.let {
            Currency(
                id = data.id,
                code = data.code,
                name = data.name,
                symbol = data.symbol,
                iconUrl = data.iconUrl,
                hex = data.hex,
                price = data.price,
                ranking = data.ranking,
                dailyVolume = data.dailyVolume,
                circulatingSupply = data.circulatingSupply,
                marketCap = data.marketCap
            )
        }
    }
}