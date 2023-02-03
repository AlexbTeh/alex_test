package com.test_crypto.data.local_db.daos

import androidx.room.*
import com.test_crypto.data.local_db.CurrencyDB
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencies: List<CurrencyDB>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currency: CurrencyDB)

    @Query("DELETE from currency")
    suspend fun deleteAll()

    @Transaction
    suspend fun reInsertCurrencies(currencies: List<CurrencyDB>) {
        deleteAll()
        insertCurrencies(currencies)
    }

    @Query("SELECT * from currency")
    fun getCurrencies(): Flow<List<CurrencyDB>>

    @Query("SELECT * from currency")
    fun getSimpleCurrencies(): List<CurrencyDB>

    @Query("SELECT * from currency")
    fun getCurrenciesWatchlist(): Flow<List<CurrencyDB>>

    @Query("SELECT * from currency where code = :code")
    fun getCurrency(code: String): Flow<CurrencyDB>

/*    @Query("UPDATE currency set isInWatchlist = :add where code = :code")
    fun updateInWatchlist(add: Boolean, code: String)*/

    @Query("SELECT * from currency where code in (:codes) and name LIKE '%' || :query || '%'")
    fun getCurrenciesByCodes(query: String, codes: List<String>): Flow<List<CurrencyDB>>

    @Query("SELECT * from currency where name LIKE '%' || :query || '%'")
    fun searchCurrencies(query: String): Flow<List<CurrencyDB>>

    @Query("SELECT * from currency where code in (:codes)")
    suspend fun getCurrenciesIn(codes: List<String>): List<CurrencyDB>
}