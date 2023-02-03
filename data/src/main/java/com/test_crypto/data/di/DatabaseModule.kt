package com.test_crypto.data.di

import android.content.Context
import com.test_crypto.data.local_db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context : Context) : AppDatabase = AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun providesCurrenciesDao(appDatabase: AppDatabase) = appDatabase.currenciesDao()

    @Provides
    @Singleton
    fun providesWatchListDao(appDatabase: AppDatabase) = appDatabase.watchListDao()
}