package com.test_crypto

import com.test_crypto.domain.events.EventChanges
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideDataChanges(): EventChanges = EventChanges()
}