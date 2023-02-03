package com.test_crypto.core_ui.styles

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil.load

sealed class Image {
    data class Resource(@DrawableRes val resource: Int) : Image()
    data class Network(val url: String) : Image()
    data class BitmapResource(val bitmap : Bitmap) : Image()
    data class UriSource(val url: String) : Image()
}


fun ImageView.load(image : Image) {
    when (image) {
        is Image.Resource -> {
            this.load(image.resource)
        }

        is Image.Network -> {
            this.load(image.url)
        }

        is Image.BitmapResource -> {
            this.load(image.bitmap)
        }
        is Image.UriSource -> {
            this.load(Uri.parse(image.url))
        }
    }
}

