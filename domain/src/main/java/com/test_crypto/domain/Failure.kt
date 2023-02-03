package com.test_crypto.domain

import android.util.Log
import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

sealed class Failure(
    val e: Exception?,
    val errorString: String? = null,
    @StringRes open val errorRes: Int? = null,
    // val body : TypedGeneralHttpError? = null
) {
    class NoInternetConnectionError(e: Exception) : Failure(e)
    class CertificateExpirationError(e: Exception) : Failure(e)
    sealed class HttpError(e: Exception, errorString: String?) : Failure(e, errorString) {
        class RequestHttpUnauthorizedError(e: Exception, errorString: String?) :
            HttpError(e, errorString)

        class RequestHttpNotFoundError(e: Exception, errorString: String?) :
            HttpError(e, errorString)

        class RequestTooManyRequests(e: Exception, errorString: String?) :
            HttpError(e, errorString)

        class RequestRestricted(e: Exception, errorString: String?) :
            HttpError(e, errorString)

        class RequestHttpError(
            e: Exception,
            errorString: String?,
            val body: TypedGeneralHttpError? = null
        ) : HttpError(e, errorString)
    }

    open class RequestGeneralServerError(e: Exception) : Failure(e)

    object None : Failure(NullPointerException(), "")

    //use for feature errors
    open class FeatureError(@StringRes override val errorRes: Int? = null) :
        Failure(RuntimeException(), errorRes = errorRes)
}

fun <T> Throwable.getRestError(): Failure {
    println("getRestError $this ")
    return when (this) {
        is UnknownHostException -> {
            Failure.NoInternetConnectionError(this)
        }
        is SSLHandshakeException -> {
            Failure.CertificateExpirationError(this)
        }
        is IOException -> {
            Failure.NoInternetConnectionError(this)
        }

        is HttpException -> {
            val errorBody = this.response()?.errorBody()?.string()
            println("Failure errorBody " + errorBody)
            when (this.code()) {
                401 -> Failure.HttpError.RequestHttpUnauthorizedError(
                    this,
                    errorBody
                )
                ;
                404 -> Failure.HttpError.RequestHttpNotFoundError(
                    this,
                    errorBody
                )
                ;
                429 -> Failure.HttpError.RequestTooManyRequests(
                    this,
                    errorBody
                )
                403 -> {
                    val typedError = Gson().fromJson(errorBody, TypedGeneralHttpError::class.java)
                    val errorBodyNotContainSuccess = errorBody!=null && !errorBody.contains("success")
                    return if (errorBodyNotContainSuccess) {
                        Failure.HttpError.RequestRestricted(this, errorBody)
                    } else {
                        try {
                            Failure.HttpError.RequestHttpError(
                                this,
                                errorBody,
                                typedError
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Failure.RequestGeneralServerError(this)
                        }
                    }
                }
                else -> {
                    println("Failure errorBody " + errorBody)
                    try {
                        val typedError = Gson().fromJson(errorBody, TypedGeneralHttpError::class.java)
                        Failure.HttpError.RequestHttpError(
                            this,
                            errorBody,
                            typedError
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Failure.RequestGeneralServerError(this)
                    }
                }
            }
        }

        else -> Failure.RequestGeneralServerError(Exception(this))
    }
}

fun Exception.getRestError(): Failure {
    Log.d("getRestError", this.toString())
    return when (this) {
        is UnknownHostException -> {
            Failure.NoInternetConnectionError(this)
        }
        is SSLHandshakeException -> {
            Failure.CertificateExpirationError(this)
        }
        is IOException -> {
            Failure.NoInternetConnectionError(this)
        }

        is HttpException -> {
            val errorBody = this.response()?.errorBody()?.string()

            when (this.code()) {
                401 -> Failure.HttpError.RequestHttpUnauthorizedError(
                    this,
                    errorBody
                )
                ;
                404 -> Failure.HttpError.RequestHttpNotFoundError(
                    this,
                    errorBody
                )
                403 -> {
                    val typedError = Gson().fromJson(errorBody, TypedGeneralHttpError::class.java)
                    val errorBodyNotContainSuccess = errorBody!=null && !errorBody.contains("success")
                    return if (errorBodyNotContainSuccess) {
                        Failure.HttpError.RequestRestricted(this, errorBody)
                    } else {
                        try {
                            Failure.HttpError.RequestHttpError(
                                this,
                                errorBody,
                                typedError
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Failure.RequestGeneralServerError(this)
                        }
                    }
                }
                else -> {
                    println("Failure errorBody " + errorBody)
                    try {
                        val typedError = Gson().fromJson(errorBody, TypedGeneralHttpError::class.java)
                        Failure.HttpError.RequestHttpError(
                            this,
                            errorBody,
                            typedError
                        )
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                        Failure.RequestGeneralServerError(this)
                    }
                };
            }
        }

        else -> Failure.RequestGeneralServerError(this)
    }
}


inline fun <reified T> Failure.HttpError.mapApiErrorToModel(gson: Gson): T? {
    return try {
        val responseBody = this.errorString
        val errorModel: T = gson.fromJson(responseBody, T::class.java)
        errorModel
    } catch (e: Exception) {
        null
    }
}

@Keep
data class TypedGeneralHttpError(
    val success: Boolean,
    val error: Error? = null,
    val message: String? = null,
    val errors: HashMap<String, List<String>>? = null
){
    data class Error(val code : Int, val message: String? = null){}
}