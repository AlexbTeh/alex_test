package com.test_crypto.data.local_db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.test_crypto.data.local_db.converters.IntListConverter

@Entity(tableName = "currency")
class CurrencyDB(
    @PrimaryKey
    var id: Int,
    var code: String? = null,
    var name: String? = null,
    var symbol: String? = null,
    var iconUrl: String? = null,
    var hex: String? = null,
    var price: String? = null,
    var ranking: Int? = null,
    var dailyVolume: String? = null,
    var circulatingSupply: String? = null,
    var marketCap: String? = null
)