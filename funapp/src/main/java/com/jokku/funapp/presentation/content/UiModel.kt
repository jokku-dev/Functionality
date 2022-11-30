package com.jokku.funapp.presentation.content

import androidx.annotation.DrawableRes
import com.jokku.funapp.R
import com.jokku.funapp.presentation.adapter.RecyclerAdapter
import com.jokku.funapp.presentation.fragment.Communicator

abstract class UiModel<T>(private val firstText: String, private val secondText: String) {
    fun setText(textView: TextSetter) = textView.set(getText())
    open fun show(communicator: Communicator<T>) =
        communicator.showState(State.Initial(getText(), getIconResId()))

    open fun matches(id: T): Boolean = false
    open fun same(model: UiModel<T>): Boolean = false
    open fun changeStatus(listener: RecyclerAdapter.FavoriteItemClickListener<T>) = Unit
    protected open fun getText() = "$firstText\n$secondText"

    @DrawableRes
    protected abstract fun getIconResId(): Int
}

class BaseUiModel<T>(firstText: String, secondText: String) : UiModel<T>(firstText, secondText) {
    override fun getIconResId() = R.drawable.ic_outline_favorite_border_34
}

class FavoriteUiModel<T>(private val id: T, firstText: String, secondText: String) :
    UiModel<T>(firstText, secondText) {
    override fun changeStatus(listener: RecyclerAdapter.FavoriteItemClickListener<T>) {
        listener.changeStatus(id)
    }

    override fun matches(id: T): Boolean = this.id == id
    override fun same(model: UiModel<T>): Boolean = model is FavoriteUiModel<T> && model.id == id
    override fun getIconResId() = R.drawable.ic_outline_favorite_34
}

class FailedUiModel<T>(private val error: String) : UiModel<T>(error, "") {
    override fun getText() = error
    override fun getIconResId() = 0
    override fun show(communicator: Communicator<T>) =
        communicator.showState(State.Failed(getText(), getIconResId()))
}