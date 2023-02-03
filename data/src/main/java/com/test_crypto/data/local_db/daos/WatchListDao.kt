package com.test_crypto.data.local_db.daos

import androidx.room.*
import com.test_crypto.data.local_db.WatchListDB
import com.test_crypto.domain.Empty
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchList(currencies: List<WatchListDB>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchList(currency: WatchListDB)

    @Query("DELETE from watchlist where id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * from watchlist")
    fun getWatchLists(): Flow<List<WatchListDB>>
}