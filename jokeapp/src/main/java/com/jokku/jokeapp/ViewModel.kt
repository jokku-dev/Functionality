package com.jokku.jokeapp


class ViewModel(private val model: Model) {
    private var callback: TextCallback? = null

    fun init(callback: TextCallback) {
        this.callback = callback
        model.init(object : ResultCallback {
            override fun provideSuccess(data: UiMapper) = callback.provideText(data.toUiText())
            override fun provideError(error: Failure) = callback.provideText(error.getMessage())
        })
    }

    fun getJoke() {
        model.getJoke()
    }

    fun clear() {
        callback = null
        model.clear()
    }
}

interface TextCallback {
    fun provideText(text: String)
}