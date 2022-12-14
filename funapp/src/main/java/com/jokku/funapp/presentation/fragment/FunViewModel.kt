package com.jokku.funapp.presentation.fragment

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import com.jokku.funapp.domain.DomainItem
import com.jokku.funapp.domain.Interactor
import com.jokku.funapp.presentation.content.State
import com.jokku.funapp.presentation.content.UiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface FunViewModel<T> : FunItemViewModel, FunListViewModel<T>
interface FunItemViewModel {
    fun getItem()
    fun getItemList()
    fun changeItemStatus()
    fun chooseFavorites(favorites: Boolean)
    fun observe(owner: LifecycleOwner, observer: Observer<State>)
}
interface FunListViewModel<T> {
    fun changeItemStatus(id: T)
    fun observeList(owner: LifecycleOwner, observer: Observer<List<UiModel<T>>>)
}
interface ListChanges<T> {
    fun getList(): List<UiModel<T>>
    fun getDiffResult(): DiffUtil.DiffResult
}

abstract class BaseViewModel<T>(
    private val interactor: Interactor<T>,
    private val communicator: Communicator<T>, //for testing purpose
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main, //for testing purpose
) : ViewModel(), FunViewModel<T>, ListChanges<T> {
    override fun getList(): List<UiModel<T>> = communicator.getList()
    override fun getDiffResult(): DiffUtil.DiffResult = communicator.getDiffResult()

    override fun changeItemStatus() {
        viewModelScope.launch(dispatcher) {
            if (communicator.isState(State.INITIAL)) {
                interactor.changeIsFavorite().map().show(communicator)
                showList()
            }
        }
    }

    override fun changeItemStatus(id: T) {
        viewModelScope.launch(dispatcher) {
            interactor.removeItem(id)
            showList()
        }
    }

    override fun getItem() {
        viewModelScope.launch(dispatcher) {
            communicator.showState(State.Progress)
            interactor.getItem().map().show(communicator)
        }
    }

    override fun getItemList() {
        viewModelScope.launch(dispatcher) {
            showList()
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<State>) {
        communicator.observe(owner, observer)
    }

    override fun observeList(owner: LifecycleOwner, observer: Observer<List<UiModel<T>>>) {
        communicator.observeList(owner, observer)
    }

    override fun chooseFavorites(favorites: Boolean) {
        interactor.getFavorites(favorites)
    }

    private suspend fun showList() =
        communicator.showDataList(interactor.getItemList().toUiModelList())
}

fun <T> List<DomainItem<T>>.toUiModelList() = map { it.map() }

class JokesViewModel(
    interactor: Interactor<Int>,
    communicator: Communicator<Int>
) : BaseViewModel<Int>(interactor, communicator)

class QuotesViewModel(
    interactor: Interactor<String>,
    communicator: Communicator<String>
) : BaseViewModel<String>(interactor, communicator)