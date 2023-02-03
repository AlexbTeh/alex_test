package com.test_crypto.data.commons

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun String.toDateOrNull(pattern: String): Long? {
    val datePattern = SimpleDateFormat(pattern)
    return try {
        datePattern.parse(this)?.time
    } catch (ex: Exception) {
        null
    }
}