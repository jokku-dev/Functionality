package com.jokku.jokeapp.model

import androidx.lifecycle.*
import com.jokku.jokeapp.data.Model
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val model: Model,
    private val communicator: Communicator,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
): ViewModel() {

    fun changeJokeStatus() = viewModelScope.launch(dispatcher) {
        model.changeJokeStatus()?.let {
            communicator.showData(it.getData())
        }
    }

    fun getJoke() = viewModelScope.launch(dispatcher) {
        communicator.showData(model.getJoke().getData())
    }

    fun chooseFavorites(isFavorite: Boolean) {
        model.chooseDataSource(isFavorite)
    }

    fun observe(owner: LifecycleOwner, observer: Observer<Pair<String, Int>>) {
        communicator.observe(owner, observer)
    }
}