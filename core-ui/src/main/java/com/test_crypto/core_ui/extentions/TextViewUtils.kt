package com.test_crypto.core_ui.extentions

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.text.InputFilter
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

fun formatPercentage(value: Double): String {
    val format: NumberFormat = NumberFormat.getPercentInstance()
    format.minimumFractionDigits = 2
    format.maximumFractionDigits = 2

    return format.format(value)
}

fun prettyCount(number: String): String? {
    val suffix = charArrayOf(' ', 'K', 'M', 'B', 'T', 'P', 'E')
    val numValue = number.toDouble()
    val value = floor(log10(numValue)).toInt()
    val base = value / 3
    return if (value >= 3 && base < suffix.size) {
        DecimalFormat("#0.00").format(numValue / 10.0.pow((base * 3).toDouble())) + suffix[base]
    } else {
        DecimalFormat().format(numValue)
    }
}

fun formatWithPrecision(number: String="0.0", count: Int = 4): String {
    val format: NumberFormat = NumberFormat.getNumberInstance()
    val currentNumber = number.replace(",", "")
    when {
        currentNumber.toDouble() >= 1.0 -> {
            format.minimumFractionDigits = count
            format.maximumFractionDigits = count
        }

        currentNumber.toDouble() < 1.0 && number.toDouble() > 0.0 -> {
            format.minimumFractionDigits = 2
            format.maximumFractionDigits = count
        }
        currentNumber.toDouble() == 0.00 -> {
            format.minimumFractionDigits = 2
            format.maximumFractionDigits = count
        }
        else -> {
            format.roundingMode = RoundingMode.FLOOR
            format.minimumFractionDigits = 0
            format.maximumFractionDigits = count
        }
    }

    return format.format(currentNumber.toDouble()).replace(",", "")
}

fun formatNumber(number: String?="0.0", count: Int = 4): String {
    return if (number != null && number.toDouble() % 1.0 != 0.0)
        String.format("%s", number.toDouble())
    else
        String.format("%.0f", number?.toDouble())
}

fun formatNumberWithSymbol(number: String? = "0.0", symbol: String, count: Int = 2): String {
    val format: NumberFormat = NumberFormat.getNumberInstance()
    when {
        number != null && number.toDouble() >= 1.0 -> {
            format.minimumFractionDigits = count
            format.maximumFractionDigits = count
        }

        number != null && number.toDouble() < 1.0 && number.toDouble() > 0.0 -> {
            format.minimumFractionDigits = 2
            format.maximumFractionDigits = count
        }
        number != null && number.toDouble() == 0.00 -> {
            format.minimumFractionDigits = 2
            format.maximumFractionDigits = count
        }
        else -> {
            format.roundingMode = RoundingMode.FLOOR
            format.minimumFractionDigits = 0
            format.maximumFractionDigits = count
        }
    }

    return """$symbol${format.format(number?.toDouble())}"""
}

fun shareImageWithView(activity: Activity, view: View) {
    val bitmap = loadBitmapFromView(view)
    bitmap?.let {
        val uri = saveImage(activity, it)
        uri?.let { uri1 ->
            shareImageUri(activity, uri1)
        }
    }
}


private fun saveImage(activity: Activity, image: Bitmap): Uri? {
    val imagesFolder = File(activity.cacheDir, "oobit_images")
    var uri: Uri? = null
    try {
        imagesFolder.mkdirs()
        val file = File(imagesFolder, "shared_image.png")
        val stream = FileOutputStream(file)
        image.compress(Bitmap.CompressFormat.PNG, 90, stream)
        stream.flush()
        stream.close()
        uri = activity.let { FileProvider.getUriForFile(it, "com.oobit.dev.fileprovider", file) }
    } catch (e: IOException) {

    }
    return uri
}

fun loadBitmapFromView(v: View): Bitmap? {
    val b = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
    val c = Canvas(b)
    v.draw(c)
    return b
}

private fun shareImageUri(activity: Activity, uri: Uri) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.type = "image/png"
    activity.startActivity(intent)
}

fun AppCompatTextView.limitLength(maxLength: Int) {
    filters = arrayOf(InputFilter.LengthFilter(maxLength))
}