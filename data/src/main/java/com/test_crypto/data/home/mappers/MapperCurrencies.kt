package com.test_crypto.data.home.mappers

import com.test_crypto.data.commons.MapperSync
import com.test_crypto.data.home.dto.CurrencyDTO
import com.test_crypto.data.local_db.CurrencyDB
import com.test_crypto.domain.home.entities.Currency

class MapperCurrencies : MapperSync<List<CurrencyDTO>, List<Currency>, List<CurrencyDB>> {
    override fun map(data: List<CurrencyDTO>): List<Currency> {
        return data.map {
            MapperCurrency().map(it)
        }
    }

    override fun mapToDb(data: List<CurrencyDTO>): List<CurrencyDB> {
        return data.map {
            MapperCurrency().mapToDb(it)
        }
    }

    override fun mapDbToEntity(data: List<CurrencyDB>?): List<Currency> {
        return data?.mapNotNull{
            MapperCurrency().mapDbToEntity(it)
        } ?: mutableListOf()
    }
}