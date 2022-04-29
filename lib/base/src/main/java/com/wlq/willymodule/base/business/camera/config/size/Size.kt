package com.wlq.willymodule.base.business.camera.config.size

import android.annotation.TargetApi
import android.hardware.Camera
import android.os.Build
import androidx.annotation.IntRange

class Size private constructor(
    @param:IntRange(from = 1) val width: Int,
    @param:IntRange(from = 0) val height: Int
) {

    private var area = -1

    private var ratio = 0.0

    fun getArea(): Int {
        if (area == -1) {
            area = width * height
        }
        return area
    }

    fun ratio(): Double {
        if (ratio == 0.0 && width != 0) {
            ratio = height.toDouble() / width
        }
        return ratio
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val size = other as Size
        return if (width != size.width) false else height == size.height
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        return result
    }

    override fun toString(): String {
        return "($width, $height)"
    }

    companion object {

        fun of(@IntRange(from = 1) width: Int, @IntRange(from = 0) height: Int): Size = Size(width, height)

        fun fromList(cameraSizes: List<Camera.Size>): List<Size> {
            return cameraSizes.map { of(it.width, it.height) }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        fun fromList(cameraSizes: Array<android.util.Size>): List<Size> {
            return cameraSizes.map { of(it.width, it.height) }
        }
    }
}