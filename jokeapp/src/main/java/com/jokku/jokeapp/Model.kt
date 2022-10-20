package com.jokku.jokeapp

interface Model {
    fun init(callback: ResultCallback)
    fun getJoke()
    fun clear()
}

class TestModel(private val resourceManager: ResourceManager) : Model {
    private var callback: ResultCallback? = null
    private var count = 0
    private val noConnection = NoConnection(resourceManager)
    private val serviceUnavailable = ServiceUnavailable(resourceManager)

    override fun init(callback: ResultCallback) {
        this.callback = callback
    }

    override fun getJoke() {
        Thread {
            Thread.sleep(1000)
            when (count) {
                0 -> callback?.provideSuccess(Joke("testText", "testPunchline"))
                1 -> callback?.provideError(noConnection)
                2 -> callback?.provideError(serviceUnavailable)
            }
            count++
            if (count == 3) count = 0
        }.start()
    }

    override fun clear() {
        callback = null
    }
}

interface ResultCallback {
    fun provideSuccess(data: UiMapper)
    fun provideError(error: Failure)
}