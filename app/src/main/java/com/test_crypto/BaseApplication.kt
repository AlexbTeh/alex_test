package com.test_crypto

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application(), ImageLoaderFactory {

    companion object {
        lateinit var instance: BaseApplication
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .componentRegistry {
                add(SvgDecoder(this@BaseApplication))
            }
            .build()
    }

}