package com.test_crypto.feature_price_details.di

import com.test_crypto.feature_price_details.PriceDetailsNavContractImpl
import com.test_crypto.nav.PricesDetailsNavContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PricesDetailsModule {
    @Singleton
    @Provides
    fun providesPricesDetailsNavContract() : PricesDetailsNavContract = PriceDetailsNavContractImpl()
}