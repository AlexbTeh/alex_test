package com.test_crypto.data.di

import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val apiKey = "98d3fb39-9f13-433a-900e-10a2ea0d0ba3"
        val request = chain.request()
        var token: String? = null
        runBlocking {
            token = "token"
        }

        val builder = request.newBuilder()
        builder.header("X-CMC_PRO_API_KEY", apiKey);

        Log.d("AuthorInterceptor","this got")

/*        if (response.code == 401 && !token.isNullOrEmpty()) {
            currentUser.onSessionExpired.invoke()
        }*/
        return chain.proceed(builder.build())
    }
}