package com.wlq.willymodule.base.business.camera.config.convert

import android.media.Image

interface ImageRawDataConverter {

    fun convertToNV21(image: Image): ByteArray
}