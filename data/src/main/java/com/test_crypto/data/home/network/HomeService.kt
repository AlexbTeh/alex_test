package com.test_crypto.data.home.network

import com.test_crypto.data.home.dto.CurrenciesResponse
import com.test_crypto.data.home.dto.CurrencyResponse
import com.test_crypto.data.home.dto.SuccessDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface HomeService {
    @GET("v1/cryptocurrency/listings/latest")
    suspend fun getCurrencies(@QueryMap params: HashMap<String, Any>): CurrenciesResponse

    @GET("v1/cryptocurrency/info")
    suspend fun getCurrency(@QueryMap params: HashMap<String, Any>): CurrencyResponse

    @POST("v1/watchlist")
    suspend fun addToWatchList(@Body params: HashMap<String, Any>): SuccessDTO
}