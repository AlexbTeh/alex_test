package com.test_crypto.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.test_crypto.data.local_db.converters.DoubleListConverter
import com.test_crypto.data.local_db.converters.IntListConverter
import com.test_crypto.data.local_db.converters.StringListConverter
import com.test_crypto.data.local_db.daos.CurrencyDao
import com.test_crypto.data.local_db.daos.WatchListDao


@Database(
    entities = [CurrencyDB::class, WatchListDB::class],
    version = 1
)
@TypeConverters(IntListConverter::class, DoubleListConverter::class, StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun currenciesDao(): CurrencyDao

    abstract fun watchListDao(): WatchListDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "crypto.db"
            ).fallbackToDestructiveMigration()
                .setJournalMode(JournalMode.AUTOMATIC)
                .build()

    }
}