package com.jokku.mvvmcustom

class ViewModel(private val model: Model) {
    private var textObservable: TextObservable? = null

    private val textCallback = object : TextCallback {
        override fun updateText(str: String) {
            textObservable?.postValue(str)
        }
    }

    fun init(textObservable: TextObservable) {
        this.textObservable = textObservable
    }

    fun startCounting() {
        model.start(textCallback)
    }

    fun stopCounting() {
        model.stop()
    }

    fun resetCounting() {
        model.reset()
    }
}

class TextObservable {
    private lateinit var textCallback: TextCallback

    fun observe(textCallback: TextCallback) {
        this.textCallback = textCallback
    }

    fun postValue(text: String) {
        textCallback.updateText(text)
    }
}

interface TextCallback {
    fun updateText(str: String)
}