package com.jokku.jokeapp.model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

interface Communicator {
    fun showData(data: Pair<String, Int>)
    fun observe(owner: LifecycleOwner, observer: Observer<Pair<String, Int>>)
}

class BaseCommunicator : Communicator {
    private val liveData = MutableLiveData<Pair<String, Int>>()

    override fun showData(data: Pair<String, Int>) {
        liveData.value = data
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<Pair<String, Int>>) {
        liveData.observe(owner, observer)
    }
}