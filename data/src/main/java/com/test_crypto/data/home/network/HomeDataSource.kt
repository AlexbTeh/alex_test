package com.test_crypto.data.home.network

import com.test_crypto.data.home.dto.CurrenciesResponse
import com.test_crypto.data.home.dto.CurrencyResponse
import com.test_crypto.data.home.dto.SuccessDTO
import java.text.SimpleDateFormat
import java.util.*

class HomeDataSource(private val retrofit: HomeService) {
    suspend fun getCurrencies(start: Int): CurrenciesResponse {
        val params = HashMap<String, Any>()
        params["start"] = start
        params["limit"] = 50
        params["convert"] = "USD"
        return retrofit.getCurrencies(params)
    }
    suspend fun getCurrency(id: Int): CurrencyResponse {
        val params = HashMap<String, Any>()
        params["id"] = id.toString()
        return retrofit.getCurrency(params)
    }

    suspend fun addToWatchList(currencyCode: String): SuccessDTO {
        val params = HashMap<String, Any>()
        params["currency_code"] = currencyCode
        return retrofit.addToWatchList(params)
    }
}