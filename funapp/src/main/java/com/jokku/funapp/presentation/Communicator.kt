package com.jokku.funapp.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

interface Communicator<T> : StateCommunicator, ListCommunicator<T>

interface StateCommunicator {
    fun isState(type: Int): Boolean
    fun showState(state: BaseFunViewModel.State)
    fun observe(owner: LifecycleOwner, observer: Observer<BaseFunViewModel.State>)
}

interface ListCommunicator<T> {
    fun getList(): List<UiModel<T>>
    fun removeItem(id: T): Int
    fun showDataList(list: List<UiModel<T>>)
    fun observeList(owner: LifecycleOwner, observer: Observer<List<UiModel<T>>>)
}

class BaseCommunicator<T> : Communicator<T> {
    private val liveData = MutableLiveData<BaseFunViewModel.State>()
    private val listLiveData = MutableLiveData<ArrayList<UiModel<T>>>()

    override fun isState(type: Int): Boolean = liveData.value?.isType(type) ?: false

    override fun getList(): List<UiModel<T>> = listLiveData.value ?: emptyList()
    override fun removeItem(id: T): Int {
        val found = listLiveData.value?.find {
            it.matches(id)
        }
        val position = listLiveData.value?.indexOf(found) ?: -1
        found?.let {
            listLiveData.value?.remove(it)
        }
        return position
    }

    override fun showState(state: BaseFunViewModel.State) {
        liveData.value = state
    }

    override fun showDataList(list: List<UiModel<T>>) {
        listLiveData.value = ArrayList(list)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<BaseFunViewModel.State>) {
        liveData.observe(owner, observer)
    }

    override fun observeList(owner: LifecycleOwner, observer: Observer<List<UiModel<T>>>) {
        listLiveData.observe(owner, observer)
    }
}