package com.test_crypto.core_ui.styles

import android.content.Context
import androidx.annotation.StringRes
import java.lang.StringBuilder

sealed class Text {
    data class Simple(val value: String) : Text()
    data class Resource(@StringRes val resource: Int, val arguments: List<Text> = mutableListOf()) :
        Text()
}

fun Text.getStringText(context: Context): String {
    return when (this) {
        is Text.Resource -> {
            if(arguments.isNotEmpty()){
                println("Text resource: ${arguments.toString()}")
                context.getStringWithArgs(this.resource, arguments)
            } else {
                context.getString(this.resource)
            }
        }
        is Text.Simple -> this.value
    }
}

fun Text.Simple.append(value : String) : Text {
    return Text.Simple(StringBuilder(this.value).append(value).toString())
}

fun Context.getStringWithArgs(@StringRes resourceId: Int, arguments: List<Text>): String {
    return if (!arguments.isNullOrEmpty()) {
        val mappedArguments = arguments.map {
            when (it) {
                is Text.Simple -> it.value
                is Text.Resource -> this.getStringWithArgs(it.resource, it.arguments)
            }
        }
        String.format(this.getString(resourceId), *mappedArguments.toTypedArray())
    } else {
        this.getString(resourceId)
    }
}

