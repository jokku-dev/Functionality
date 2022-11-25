package com.jokku.funapp.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jokku.funapp.data.cache.PersistentDataSource

class MainViewModel(
    private val screenPosition: ScreenPosition,
    private val navigation: NavigationCommunicator
) : ViewModel() {

    fun init() {
        navigateTo(screenPosition.load())
    }

    fun save(position: Int) {
        screenPosition.save(position)
        navigateTo(position)
    }

    fun observe(owner: LifecycleOwner, navigate: (Int) -> Unit) {
        navigation.observe(owner, navigate)
    }

    private fun navigateTo(position: Int) {
        navigation.navigateTo(position)
    }
}

interface NavigationCommunicator {
    fun observe(owner: LifecycleOwner, navigate: (Int) -> Unit)
    fun navigateTo(position: Int)

    class Base : NavigationCommunicator {
        private val mutableLiveData = MutableLiveData<Int>()

        override fun observe(owner: LifecycleOwner, navigate: (Int) -> Unit) {
            mutableLiveData.observe(owner) { position -> navigate.invoke(position) }

        }

        override fun navigateTo(position: Int) {
            mutableLiveData.value = position
        }
    }
}

interface ScreenPosition {
    fun save(position: Int)
    fun load(): Int

    class Base(private val persistentDataSource: PersistentDataSource) : ScreenPosition {
        override fun save(position: Int) {
            persistentDataSource.save(position, NAME, KEY)
        }

        override fun load(): Int = persistentDataSource.load(NAME, KEY)

        private companion object {
            const val NAME = "screenPosition"
            const val KEY = "screenPositionKey"
        }
    }
}