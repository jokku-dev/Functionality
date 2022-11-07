package com.jokku.jokeapp.presentation

import androidx.annotation.DrawableRes
import com.jokku.jokeapp.R

abstract class JokeUiModel(private val setup: String, private val punchline: String) {
    open fun show(communicator: Communicator) =
        communicator.showState(MainViewModel.State.Initial(getText(), getIconResId()))

    protected open fun getText() = "$setup\n$punchline"

    @DrawableRes
    protected abstract fun getIconResId(): Int
}

class BaseJokeUiModel(setup: String, punchline: String) : JokeUiModel(setup, punchline) {
    override fun getIconResId() = R.drawable.ic_outline_favorite_border_34
}

class FavoriteJokeUiModel(setup: String, punchline: String) : JokeUiModel(setup, punchline) {
    override fun getIconResId() = R.drawable.ic_outline_favorite_34
}

class FailedJokeUiModel(private val error: String) : JokeUiModel(error, "") {
    override fun getText() = error
    override fun getIconResId() = 0
    override fun show(communicator: Communicator) = communicator.showState(
        MainViewModel.State.Failed(getText(), getIconResId())
    )
}