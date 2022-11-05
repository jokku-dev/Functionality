package com.jokku.jokeapp.presentation.model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

interface Communicator {
    fun showState(state: MainViewModel.State)
    fun isState(type: Int): Boolean
    fun observe(owner: LifecycleOwner, observer: Observer<MainViewModel.State>)
}

class BaseCommunicator : Communicator {
    private val liveData = MutableLiveData<MainViewModel.State>()

    override fun showState(state: MainViewModel.State) {
        liveData.value = state
    }

    override fun isState(type: Int): Boolean {
        return liveData.value?.isType(type) ?: false
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<MainViewModel.State>) {
        liveData.observe(owner, observer)
    }
}