package com.jokku.funapp.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil

interface Communicator<T> : StateCommunicator, ListCommunicator<T>
interface StateCommunicator {
    fun isState(type: Int): Boolean
    fun showState(state: State)
    fun observe(owner: LifecycleOwner, observer: Observer<State>)
}
interface ListCommunicator<T> {
    fun getList(): List<UiModel<T>>
    fun getDiffResult(): DiffUtil.DiffResult
    fun showDataList(list: List<UiModel<T>>)
    fun observeList(owner: LifecycleOwner, observer: Observer<List<UiModel<T>>>)
}

class BaseCommunicator<T> : Communicator<T> {
    private val liveData = MutableLiveData<State>()
    private val listLiveData = MutableLiveData<ArrayList<UiModel<T>>>()
    private lateinit var diffResult: DiffUtil.DiffResult
    override fun getDiffResult() = diffResult

    override fun isState(type: Int): Boolean = liveData.value?.isType(type) ?: false
    override fun getList(): List<UiModel<T>> = listLiveData.value ?: emptyList()

    override fun showState(state: State) {
        liveData.value = state
    }

    override fun showDataList(list: List<UiModel<T>>) {
        val callback = DiffUtilCallback(listLiveData.value ?: emptyList(), list)
        diffResult = DiffUtil.calculateDiff(callback)
        listLiveData.value = ArrayList(list)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<State>) {
        liveData.observe(owner, observer)
    }

    override fun observeList(owner: LifecycleOwner, observer: Observer<List<UiModel<T>>>) {
        listLiveData.observe(owner, observer)
    }
}