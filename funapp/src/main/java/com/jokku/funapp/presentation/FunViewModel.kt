package com.jokku.funapp.presentation

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jokku.funapp.domain.DomainItem
import com.jokku.funapp.domain.FunInteractor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface FunViewModel<T> : FunItemViewModel, FunListViewModel<T>

interface FunItemViewModel {
    fun getItem()
    fun getItemList()
    fun changeItemStatus()
    fun chooseFavorites(favorites: Boolean)
    fun observe(owner: LifecycleOwner, observer: Observer<BaseFunViewModel.State>)
}
interface FunListViewModel<T> {
    fun changeListItemStatus(id: T): Int
    fun observeList(owner: LifecycleOwner, observer: Observer<List<UiModel<T>>>)
}

class BaseFunViewModel<T>(
    private val interactor: FunInteractor<T>,
    private val communicator: Communicator<T>, //for testing purpose
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main, //for testing purpose
) : ViewModel(), FunViewModel<T> {

    @SuppressLint("SuspiciousIndentation")
    override fun changeItemStatus() {
        viewModelScope.launch(dispatcher) {
            if (communicator.isState(State.INITIAL))
                interactor.changeIsFavorite().map().show(communicator)
            communicator.showDataList(interactor.getItemList().toUiModelList())
        }
    }

    override fun changeListItemStatus(id: T): Int {
        val position = communicator.removeItem(id)
        viewModelScope.launch(dispatcher) {
            interactor.removeItem(id)
        }
        return position
    }

    override fun getItem() {
        viewModelScope.launch(dispatcher) {
            communicator.showState(State.Progress)
            interactor.getItem().map().show(communicator)
        }
    }

    override fun getItemList() {
        viewModelScope.launch(dispatcher) {
            communicator.showDataList(interactor.getItemList().toUiModelList())
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<State>) {
        communicator.observe(owner, observer)
    }

    override fun observeList(owner: LifecycleOwner, observer: Observer<List<UiModel<T>>>) {
        communicator.observeList(owner, observer)
    }

    override fun chooseFavorites(favorites: Boolean) {
        interactor.chooseFavorites(favorites)
    }

    private fun List<DomainItem<T>>.toUiModelList() = map { it.map() }

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