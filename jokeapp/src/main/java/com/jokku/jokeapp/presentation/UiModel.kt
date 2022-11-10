package com.jokku.jokeapp.presentation

import androidx.annotation.DrawableRes
import com.jokku.jokeapp.R

abstract class UiModel(private val firstText: String, private val secondText: String) {
    open fun show(communicator: Communicator) =
        communicator.showState(MainViewModel.State.Initial(getText(), getIconResId()))

    protected open fun getText() = "$firstText\n$secondText"

    @DrawableRes
    protected abstract fun getIconResId(): Int
}

class BaseUiModel(firstText: String, secondText: String) : UiModel(firstText, secondText) {
    override fun getIconResId() = R.drawable.ic_outline_favorite_border_34
}

class FavoriteUiModel(firstText: String, secondText: String) : UiModel(firstText, secondText) {
    override fun getIconResId() = R.drawable.ic_outline_favorite_34
}

class FailedUiModel(private val error: String) : UiModel(error, "") {
    override fun getText() = error
    override fun getIconResId() = 0
    override fun show(communicator: Communicator) = communicator.showState(
        MainViewModel.State.Failed(getText(), getIconResId())
    )
}