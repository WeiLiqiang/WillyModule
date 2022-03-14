package com.wlq.willymodule.base.camera.listener

import android.content.Context
import android.util.SparseIntArray
import android.view.Display
import android.view.OrientationEventListener
import android.view.Surface

abstract class DisplayOrientationDetector protected constructor(context: Context) {

    private val orientationEventListener: OrientationEventListener

    companion object {
        private val DISPLAY_ORIENTATIONS = SparseIntArray()

        init {
            DISPLAY_ORIENTATIONS.put(Surface.ROTATION_0, 0)
            DISPLAY_ORIENTATIONS.put(Surface.ROTATION_90, 90)
            DISPLAY_ORIENTATIONS.put(Surface.ROTATION_180, 180)
            DISPLAY_ORIENTATIONS.put(Surface.ROTATION_270, 270)
        }
    }

    private var display: Display? = null

    var lastKnowDisplayOrientation = 0
        private set

    fun enable(display: Display?) {
        this.display = display
        orientationEventListener.enable()
        display?.let {
            dispatchOnDisplayOrientationChanged(DISPLAY_ORIENTATIONS[it.rotation])
        }
    }

    fun disable() {
        orientationEventListener.disable()
        display = null
    }

    private fun dispatchOnDisplayOrientationChanged(displayOrientation: Int) {
        lastKnowDisplayOrientation = displayOrientation
        onDisplayOrientationChanged(displayOrientation)
    }

    abstract fun onDisplayOrientationChanged(displayOrientation: Int)

    init {
        orientationEventListener = object : OrientationEventListener(context) {
            private var mLastKnownRotation = -1
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN || display == null) {
                    return
                }
                val rotation = display!!.rotation
                if (mLastKnownRotation != rotation) {
                    mLastKnownRotation = rotation
                    dispatchOnDisplayOrientationChanged(DISPLAY_ORIENTATIONS[rotation])
                }
            }
        }
    }
}