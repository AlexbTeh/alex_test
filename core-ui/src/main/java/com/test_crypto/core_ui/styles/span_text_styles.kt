package com.test_crypto.core_ui.styles

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.res.ResourcesCompat
import java.util.*

sealed class SpanText(
    open val foregroundColor: ItemColor,
    open val isUnderline: Boolean = true
) {
    data class Click(
        val text: Text.Resource,
        val span: Text,
        override val foregroundColor: ItemColor,
        override val isUnderline: Boolean,
        val font: Int? = null,
    ) : SpanText(foregroundColor, isUnderline)

    data class Foreground(
        val text : Text.Resource,
        val span: Text,
        override val foregroundColor: ItemColor,
        override val isUnderline: Boolean = false
    ) : SpanText(foregroundColor, isUnderline)
}

fun SpanText.getSpannableText(context: Context, action: () -> Unit = {}): Spannable {
    return when (this) {
        is SpanText.Foreground -> {
            val spannedText = this.span.getStringText(context)
            val fullTextString = context.getStringWithArgs(this.text.resource, mutableListOf(
                Text.Simple(
                    spannedText
                )
            ))
            val spannableText = SpannableString(fullTextString)
            val startIndex = fullTextString.lowercase(Locale.getDefault())
                .indexOf(spannedText.lowercase(Locale.getDefault()))
            spannableText.setSpan(
                ForegroundColorSpan(foregroundColor.getColor(context)),
                startIndex,
                startIndex + spannedText.length,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannableText
        }

        is SpanText.Click -> {
            val spannedText = this.span.getStringText(context)
            val fullTextString =
                context.getStringWithArgs(this.text.resource, mutableListOf(this.span))
            val spannableText = SpannableString(fullTextString)

            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    action.invoke()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    font?.let {
                        val typeface = ResourcesCompat.getFont(context, it)
                        ds.typeface = typeface
                    }
                    ds.color = foregroundColor.getColor(context)
                    ds.isUnderlineText = isUnderline
                }
            }
            val startIndex = fullTextString.lowercase(Locale.getDefault())
                .indexOf(spannedText.lowercase(Locale.getDefault()))

            try {
                spannableText.setSpan(
                    clickableSpan,
                    startIndex,
                    startIndex + spannedText.length,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
            } catch (e: java.lang.Exception) {
            }

            spannableText
        }
    }
}