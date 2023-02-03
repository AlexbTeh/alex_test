package com.test_crypto.data.home.dto

import com.google.gson.annotations.SerializedName

data class CurrenciesResponse (
    @SerializedName("success") var success : Boolean,
    @SerializedName("data") var data : List<CurrencyDTO>
)