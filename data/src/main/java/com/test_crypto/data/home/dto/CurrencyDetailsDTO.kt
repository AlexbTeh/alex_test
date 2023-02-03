package com.test_crypto.data.home.dto

import com.google.gson.annotations.SerializedName

data class CurrencyDetailsDTO(@SerializedName("id") var id : Int,
                              @SerializedName("name") var name : String? = null,
                              @SerializedName("symbol") var symbol : String? = null,
                              @SerializedName("logo") var iconUrl : String? = null,
                              @SerializedName("description") var description: String? = null,
) {
}