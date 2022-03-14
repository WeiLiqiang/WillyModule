package com.wlq.willymodule.base.camera.enums

import androidx.annotation.IntDef

@IntDef(
    PreviewAdjustType.NONE,
    PreviewAdjustType.WIDTH_FIRST,
    PreviewAdjustType.HEIGHT_FIRST,
    PreviewAdjustType.SMALLER_FIRST,
    PreviewAdjustType.LARGER_FIRST
)
@Retention(AnnotationRetention.SOURCE)
annotation class PreviewAdjustType {
    companion object {
        /** The imagery will be stretched to fit the view  */
        const val NONE = 0

        /**
         * Use the width of view,
         * the height will be stretched to meet the aspect ratio.
         *
         * Example:
         *
         * +-------------+
         * |             |
         * |/////////////|
         * |/////////////|
         * |//  image  //|
         * |/////////////|
         * |/////////////|
         * |             |
         * +-------------+  */
        const val WIDTH_FIRST = 1

        /**
         * Use the height of view,
         * the width will be stretched to meet the aspect ratio.
         *
         * Example:
         *
         * +-------------+
         * |  /////////  |
         * |  /////////  |
         * |  /////////  |
         * |  / image /  |
         * |  /////////  |
         * |  /////////  |
         * |  /////////  |
         * +-------------+  */
        const val HEIGHT_FIRST = 2

        /**
         * Use the smaller side between height and width,
         * another will be stretched to meet the aspect ratio.
         *
         * @see .WIDTH_FIRST
         *
         * @see .HEIGHT_FIRST
         */
        const val SMALLER_FIRST = 3

        /**
         * Use the larger side between height and width,
         * another will be stretched to meet the aspect ratio.
         *
         * @see .WIDTH_FIRST
         *
         * @see .HEIGHT_FIRST
         */
        const val LARGER_FIRST = 4
    }
}