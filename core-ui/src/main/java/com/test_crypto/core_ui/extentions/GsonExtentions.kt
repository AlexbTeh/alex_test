package com.test_crypto.core_ui.extentions

import com.google.gson.Gson
import java.lang.Exception


inline fun <reified T> Map<String, List<String>>.mapTo(): T? {
    return try {
        val jsonElement = Gson().toJsonTree(this)
        Gson().fromJson(jsonElement, T::class.java)
    } catch (e: Exception) {
        null
    }
}