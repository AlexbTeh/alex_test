package com.test_crypto.data.home.mappers

import com.test_crypto.data.commons.Mapper
import com.test_crypto.data.home.dto.CurrencyDetailsDTO
import com.test_crypto.domain.home.entities.CurrencyDetails

class MapperCurrencyDetails : Mapper<CurrencyDetailsDTO?, CurrencyDetails> {
    override fun map(data: CurrencyDetailsDTO?): CurrencyDetails {
        return CurrencyDetails(
            id = data?.id?:0,
            name = data?.name,
            symbol = data?.symbol,
            iconUrl = data?.iconUrl,
            description = data?.description
        )
    }
}