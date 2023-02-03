package com.test_crypto.core_ui.extentions

import android.content.Context
import com.google.gson.Gson
import com.test_crypto.domain.Failure

fun Failure.mapApiErrorKeyToResourceString(gson: Gson, context: Context): String {
    println("mapApiErrorKeyToResourceString errorString $errorString")
    var errorMessage = ""
    val responseBody = errorString
    if (responseBody != null) {
        try {
            val apiResponseHashMap: ArrayList<*> = gson.fromJson(
                this.errorString,
                List::class.java
            ) as ArrayList<*>

            apiResponseHashMap.forEach {
                errorMessage += it.toString()
            }

        } catch (e: Exception) {
            errorMessage = context.getString(StringResLink.api_error)
            e.printStackTrace()
        }
    } else {
        errorMessage = this.e?.cause?.message ?: context.getString(StringResLink.api_error)
    }

    return errorMessage
}