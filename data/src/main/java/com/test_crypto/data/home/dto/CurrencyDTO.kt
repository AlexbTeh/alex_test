package com.test_crypto.data.home.dto

import com.google.gson.annotations.SerializedName

data class CurrencyDTO(@SerializedName("id") var id : Int,
                       @SerializedName("code") var code : String,
                       @SerializedName("name") var name : String? = null,
                       @SerializedName("symbol") var symbol : String? = null,
                       @SerializedName("logo") var iconUrl : String? = null,
                       @SerializedName("price") var price : String? = null,
                       @SerializedName("ranking") var ranking: Int? = null,
                       @SerializedName("daily_volume") var dailyVolume: String? = null,
                       @SerializedName("circulating_supply") var circulatingSupply: String? = null,
                       @SerializedName("market_cap") var marketCap: String? = null
)