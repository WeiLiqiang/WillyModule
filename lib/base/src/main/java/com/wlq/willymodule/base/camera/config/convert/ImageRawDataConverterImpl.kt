package com.wlq.willymodule.base.camera.config.convert

import android.graphics.ImageFormat
import android.media.Image
import com.wlq.willymodule.base.camera.util.ImageHelper

class ImageRawDataConverterImpl : ImageRawDataConverter {

    override fun convertToNV21(image: Image): ByteArray {
        return if (image.format == ImageFormat.YUV_420_888) {
            ImageHelper.convertYUV_420_888toNV21(image)
        } else ByteArray(0)
    }
}