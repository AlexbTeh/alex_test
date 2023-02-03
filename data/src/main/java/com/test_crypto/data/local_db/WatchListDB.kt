package com.test_crypto.data.local_db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
class WatchListDB(
    @PrimaryKey
    var id: Int,
    var symbol: String? = null,
    var name: String? = null,
    var iconUrl: String? = null,
    var description: String? = null,
)