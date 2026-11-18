package com.openroom.render

import android.app.ActivityManager
import android.content.Context

object ProxySize {
    fun forDevice(context: Context): Int {
        val memoryClass = (context.getSystemService(ActivityManager::class.java))?.memoryClass ?: 128
        return when {
            memoryClass < 192 -> 1500
            memoryClass < 384 -> 2000
            else -> 2500
        }
    }
}
