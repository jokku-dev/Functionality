package com.jokku.jokeapp.model

import androidx.annotation.DrawableRes
import com.jokku.jokeapp.R

abstract class Joke(private val setup: String, private val punchline: String) {
    fun map(callback: DisplayCallback) = callback.run {
        provideText(getComposeJoke())
        provideIconRes(getIconResId())
    }

    protected fun getComposeJoke() = "$setup\n$punchline"

    @DrawableRes
    protected abstract fun getIconResId(): Int
}

class BaseJoke(setup: String, punchline: String) : Joke(setup, punchline) {
    override fun getIconResId() = R.drawable.ic_outline_favorite_border_34
}

class FavoriteJoke(setup: String, punchline: String) : Joke(setup, punchline) {
    override fun getIconResId() = R.drawable.ic_outline_favorite_34
}

class FailedJoke(text: String) : Joke(text, "") {
    override fun getIconResId() = 0
}