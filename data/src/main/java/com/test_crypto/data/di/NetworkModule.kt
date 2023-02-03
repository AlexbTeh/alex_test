package com.test_crypto.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.test_crypto.data.BuildConfig
import com.test_crypto.data.di.annotations.OkHttpAuthInterceptor
import com.test_crypto.data.di.annotations.RetrofitAuthInterceptor
import com.test_crypto.data.home.network.HomeDataSource
import com.test_crypto.data.home.network.HomeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://pro-api.coinmarketcap.com/"

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideAuthorizationInterceptor(
    ): AuthorizationInterceptor =
        AuthorizationInterceptor()

    @Provides
    @Singleton
    @OkHttpAuthInterceptor
    fun provideOkHttpClientWithLoginInterceptor(
        authorizationInterceptor: AuthorizationInterceptor,
        builder: OkHttpClient.Builder
    ): OkHttpClient {
        builder.addInterceptor(authorizationInterceptor)
        return builder.build()
    }

    @Provides
    @Singleton
    fun providesOkHttpClientBuilder(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { _, _ -> true }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        builder.addInterceptor(loggingInterceptor)
        builder.readTimeout(40, TimeUnit.SECONDS)
        builder.connectTimeout(40, TimeUnit.SECONDS)

        return builder
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        )


    @Provides
    @Singleton
    @RetrofitAuthInterceptor
    fun provideRetrofitWithLoginInterceptor(
        @OkHttpAuthInterceptor okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls().create()
                )
            )
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun providesHomeService(@RetrofitAuthInterceptor retrofit: Retrofit): HomeService {
        return retrofit.create(HomeService::class.java)
    }

    @Provides
    fun providesHomeDataSource(service: HomeService): HomeDataSource {
        return HomeDataSource(service)
    }
}