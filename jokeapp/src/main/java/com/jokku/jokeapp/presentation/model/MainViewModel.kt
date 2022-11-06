package com.jokku.jokeapp.presentation.model

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.lifecycle.*
import com.jokku.jokeapp.domain.JokeInteractor
import com.jokku.jokeapp.presentation.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val interactor: JokeInteractor,
    private val communicator: Communicator, //for testing purpose
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main, //for testing purpose
) : ViewModel() {

    @SuppressLint("SuspiciousIndentation")
    fun changeJokeStatus() = viewModelScope.launch(dispatcher) {
        if (communicator.isState(State.INITIAL))
        interactor.changeFavorites().map().show(communicator)
    }

    fun getJoke() = viewModelScope.launch(dispatcher) {
        communicator.showState(State.Progress)
        interactor.getJoke().map().show(communicator)
    }

    fun chooseFavorites(isFavorite: Boolean) = viewModelScope.launch(dispatcher) {
        interactor.getFavoriteJoke(isFavorite)
    }

    fun observe(owner: LifecycleOwner, observer: Observer<State>) {
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
            progressBar: ShowBar,
            getBtn: EnableBtn,
            textView: SetText,
            favoriteBtn: SetImage
        ) {
            show(progressBar, getBtn)
            show(textView, favoriteBtn)
        }

        protected open fun show(progressBar: ShowBar, button: EnableBtn) {}
        protected open fun show(textView: SetText, imageBtn: SetImage) {}


        abstract class Info(private val text: String, @DrawableRes private val id: Int) : State() {
            override fun show(progressBar: ShowBar, button: EnableBtn) {
                button.enable(true)
                progressBar.show(false)
            }

            override fun show(textView: SetText, imageBtn: SetImage) {
                textView.set(text)
                imageBtn.set(id)
                imageBtn.show(true)
            }
        }

        object Progress : State() {
            override val type = PROGRESS

            override fun show(progressBar: ShowBar, button: EnableBtn) {
                progressBar.show(true)
                button.enable(false)
            }
        }

        class Initial(text: String, @DrawableRes private val id: Int) : Info(text, id) {
            override val type = INITIAL
        }
        class Failed(text: String, @DrawableRes private val id: Int) : Info(text, id) {
            override val type = FAILED
        }
    }
}