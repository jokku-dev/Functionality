package com.jokku.funapp.presentation

import androidx.annotation.DrawableRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jokku.funapp.domain.Interactor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface CommonViewModel {
    fun getItem()
    fun changeItemStatus()
    fun chooseFavorites(favorites: Boolean)
    fun observe(owner: LifecycleOwner, observer: Observer<MainViewModel.State>)
}

class MainViewModel(
    private val interactor: Interactor,
    private val communicator: Communicator, //for testing purpose
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main, //for testing purpose
) : ViewModel(), CommonViewModel {

    override fun changeItemStatus() {
        viewModelScope.launch(dispatcher) {
            if (communicator.isState(State.INITIAL))
                interactor.changePreference().map().show(communicator)
        }
    }

    override fun getItem() {
        viewModelScope.launch(dispatcher) {
            communicator.showState(State.Progress)
            interactor.getItem().map().show(communicator)
        }
    }

    override fun chooseFavorites(favorites: Boolean) {
            interactor.chooseFavorites(favorites)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<State>) {
        communicator.observe(owner, observer)
    }

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

        object Progress : State() {
            override val type = PROGRESS

            override fun show(progressBar: BarShow, button: BtnEnabler) {
                progressBar.show(true)
                button.enable(false)
            }
        }
    }
}