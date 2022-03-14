package com.wlq.willymodule.base.camera.enums

import androidx.annotation.IntDef

@IntDef(
    SensorPosition.SENSOR_POSITION_UP,
    SensorPosition.SENSOR_POSITION_UP_SIDE_DOWN,
    SensorPosition.SENSOR_POSITION_LEFT,
    SensorPosition.SENSOR_POSITION_RIGHT,
    SensorPosition.SENSOR_POSITION_UNSPECIFIED
)
@Retention(AnnotationRetention.SOURCE)
annotation class SensorPosition {

    companion object {
        const val SENSOR_POSITION_UP = 90
        const val SENSOR_POSITION_UP_SIDE_DOWN = 270
        const val SENSOR_POSITION_LEFT = 0
        const val SENSOR_POSITION_RIGHT = 180
        const val SENSOR_POSITION_UNSPECIFIED = -1
    }
}
