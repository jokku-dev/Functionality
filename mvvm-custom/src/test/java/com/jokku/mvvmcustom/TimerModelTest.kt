package com.jokku.mvvmcustom

import org.junit.Assert.assertEquals
import org.junit.Test

class TimerModelTest {
    private class TestCallback : TextCallback {
        var text = ""

        override fun updateText(str: String) {
            text = str
        }
    }

    private class TestDataSource : DataSource {
        var int: Int = Int.MIN_VALUE

        override fun saveInt(key: String, value: Int) {
            int = value
        }

        override fun getInt(key: String) = int
    }

    private class TestTimeTicker : TimeTicker {

        private var tickerCallback: TimeTicker.Callback? = null
        var state = 0

        override fun start(callback: TimeTicker.Callback, period: Long) {
            this.tickerCallback = callback
            state = 1
        }

        override fun stopOrReset() {
            tickerCallback = null
            state = 0
        }

        fun tick(times: Int) {
            for (i in 0 until times )
                tickerCallback?.tick()
        }
    }

    @Test
    fun test_start_with_saved_value() {
        val testDataSource = TestDataSource()
        val timeTicker = TestTimeTicker()
        val model = Model(testDataSource, timeTicker)
        val callback = TestCallback()
        testDataSource.saveInt("", 5)
        model.start(callback)
        timeTicker.tick(1)
        val actual = callback.text
        val expected = "6"
        assertEquals(expected, actual)
    }

    @Test
    fun test_stop_after_two_seconds() {
        val testDataSource = TestDataSource()
        val timeTicker = TestTimeTicker()
        val model = Model(testDataSource, timeTicker)
        val callback = TestCallback()
        testDataSource.saveInt("", 0)
        model.start(callback)
        timeTicker.tick(2)
        val actual = callback.text
        val expected = "2"
        assertEquals(expected, actual)

        model.stop()
        val savedCountActual = testDataSource.getInt("")
        val savedCountExpected = 2
        assertEquals(savedCountActual,savedCountExpected)
    }

}