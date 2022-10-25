package com.jokku.jokeapp.model

import androidx.annotation.DrawableRes
import com.jokku.jokeapp.data.JokeCallback
import com.jokku.jokeapp.data.Model

class ViewModel(private val model: Model) {
    private var displayCallback: DisplayCallback? = null
    private val jokeCallback = object : JokeCallback {
        override fun provide(joke: Joke) {
            displayCallback?.let {
                joke.map(it)
            }
        }
    }

    fun init(callback: DisplayCallback) {
        displayCallback = callback
        model.init(jokeCallback)
    }

    fun getJoke() {
        model.getJoke()
    }

    fun chooseFavorites(isFavorite: Boolean) {
        model.chooseDataSource(isFavorite)
    }

    fun changeJokeStatus() {
        model.changeJokeStatus(jokeCallback)
    }

    fun clear() {
        displayCallback = null
        model.clear()
    }
}

interface DisplayCallback {
    fun provideText(text: String)
    fun provideIconRes(@DrawableRes id: Int)
}