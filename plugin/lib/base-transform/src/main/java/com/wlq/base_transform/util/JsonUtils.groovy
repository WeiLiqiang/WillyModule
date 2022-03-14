package com.wlq.base_transform.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * <pre>
 *     desc  :
 * </pre>
 */
final class JsonUtils {

    static final Gson GSON = new GsonBuilder().setPrettyPrinting().create()

    static String getFormatJson(Object object) {
        return GSON.toJson(object)
    }
}
