package com.jokku.funapp.presentation

import androidx.annotation.DrawableRes

sealed class State {
    protected abstract val type: Int

    companion object {
        const val INITIAL = 0
        const val PROGRESS = 1
        const val FAILED = 2
    }

    fun isType(type: Int): Boolean = this.type == type
    fun show(
        progressBar: BarShow,
        getBtn: BtnEnabler,
        textView: TextSetter,
        favoriteBtn: ImageSetter
    ) {
        show(progressBar, getBtn)
        show(textView, favoriteBtn)
    }

    protected open fun show(progressBar: BarShow, button: BtnEnabler) {}
    protected open fun show(textView: TextSetter, imageBtn: ImageSetter) {}

    object Progress : State() {
        override val type = PROGRESS

        override fun show(progressBar: BarShow, button: BtnEnabler) {
            progressBar.show(true)
            button.enable(false)
        }
    }

    abstract class ViewState(private val text: String, @DrawableRes private val id: Int) : State() {
        override fun show(progressBar: BarShow, button: BtnEnabler) {
            button.enable(true)
            progressBar.show(false)
        }

        override fun show(textView: TextSetter, imageBtn: ImageSetter) {
            textView.set(text)
            imageBtn.set(id)
            imageBtn.show(true)
        }
    }

    class Initial(text: String, @DrawableRes private val id: Int) : ViewState(text, id) {
        override val type = INITIAL
    }

    class Failed(text: String, @DrawableRes private val id: Int) : ViewState(text, id) {
        override val type = FAILED
    }
}