package com.test_crypto.data.di.annotations

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitAuthInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitNoAuthInterceptor