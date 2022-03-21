package com.wlq.startup

interface GraphvizNodePlug {

    fun insertNode(only: String)

    fun insertNode(left: String, right: String)
}