package com.wlq.willymodule.common.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.wlq.willymodule.base.util.NetworkUtils

class NetworkChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val isConnected = NetworkUtils.isConnected()
        if (isConnected) {

        } else {

        }
    }
}