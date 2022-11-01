package com.jokku.jokeapp.model

import androidx.annotation.DrawableRes
import com.jokku.jokeapp.R

abstract class JokeUiModel(private val setup: String, private val punchline: String) {
    fun getData() = Pair(getComposeJoke(), getIconResId())

    private fun getComposeJoke() = "$setup\n$punchline"

    @DrawableRes
    protected abstract fun getIconResId(): Int
}

class BaseJokeUiModel(setup: String, punchline: String) : JokeUiModel(setup, punchline) {
    override fun getIconResId() = R.drawable.ic_outline_favorite_border_34
}

class FavoriteJokeUiModel(setup: String, punchline: String) : JokeUiModel(setup, punchline) {
    override fun getIconResId() = R.drawable.ic_outline_favorite_34
}

class FailedJokeUiModel(error: String) : JokeUiModel(error, "") {
    override fun getIconResId() = 0
}