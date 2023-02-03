package com.test_crypto.data.di

import com.test_crypto.data.home.RepositoryCurrenciesImpl
import com.test_crypto.data.home.RepositoryWatchListImpl
import com.test_crypto.domain.home.RepositoryCurrencies
import com.test_crypto.domain.home.RepositoryWatchList
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {
    @Binds
    abstract fun providesRepositoryPrices(implementation: RepositoryCurrenciesImpl): RepositoryCurrencies

    @Binds
    abstract fun providesRepositoryWatchList(implementation: RepositoryWatchListImpl): RepositoryWatchList
}