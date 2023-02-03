package com.test_crypto.data.home.dto

import com.google.gson.annotations.SerializedName
import com.test_crypto.data.home.dto.CurrencyDetailsDTO

data class CurrencyResponse(
    @SerializedName("data") var data: HashMap<String,CurrencyDetailsDTO>
)