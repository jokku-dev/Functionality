package com.jokku.mvvmcustom

class Model(
    private val dataSource: DataSource,
    private val timeTicker: TimeTicker
) {

    companion object {
        private const val SECONDS = "seconds"
    }

    private val tickerCallback = object : TimeTicker.Callback {
        override fun tick() {
            seconds++
            callback?.updateText(seconds.toString())
        }
    }
    private var callback: TextCallback? = null
    private var seconds = 0

    fun start(callback: TextCallback) {
        this.callback = callback
        seconds = if (dataSource.getInt(SECONDS) != 0) dataSource.getInt(SECONDS) else 0
        timeTicker.start(tickerCallback)
    }

    fun stop() {
        dataSource.saveInt(SECONDS, seconds)
        timeTicker.stopOrReset()
    }

    fun reset() {
        seconds = 0
        dataSource.saveInt(SECONDS, 0)
        callback?.updateText("0")
        timeTicker.stopOrReset()
    }
}