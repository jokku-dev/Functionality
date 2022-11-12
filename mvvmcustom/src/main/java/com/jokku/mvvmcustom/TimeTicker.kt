package com.jokku.mvvmcustom

import java.util.*

interface TimeTicker {
    fun start(callback: Callback, period: Long = 1000)

    fun stopOrReset()

    interface Callback {

        fun tick()
    }
}

class TimerTicker : TimeTicker {

    private var tickerCallback: TimeTicker.Callback? = null
    private var timer: Timer? = null

    override fun start(callback: TimeTicker.Callback, period: Long) {
        tickerCallback = callback
        val timerTask = object : TimerTask() {
            override fun run() {
                tickerCallback?.tick()
            }
        }
        timer = Timer()
        timer?.schedule(timerTask,0,period)
    }

    override fun stopOrReset() {
        tickerCallback = null
        timer?.cancel()
        timer = null
    }
}