package com.jokku.mvvmcustom

import android.os.Handler
import android.os.Looper

class Model(private val dataSource: DataSource) {

    companion object {
        private const val SECONDS = "seconds"
    }

    private lateinit var callback: TextCallback
    private lateinit var handler: Handler
    private var seconds = dataSource.getInt(SECONDS)

    fun start(textCallback: TextCallback) {
        callback = textCallback
        seconds = if (dataSource.getInt(SECONDS) != 0) dataSource.getInt(SECONDS) else 0
        handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                callback.updateText(seconds.toString())
                seconds++
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }

    fun stop() {
        handler.removeCallbacksAndMessages(null)
        dataSource.saveInt(SECONDS, seconds)
    }

    fun reset() {
        seconds = 0
        handler.removeCallbacksAndMessages(null)
        dataSource.saveInt(SECONDS, 0)
        callback.updateText("0")
    }
}