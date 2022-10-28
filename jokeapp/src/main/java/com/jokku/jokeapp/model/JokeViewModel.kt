package com.jokku.jokeapp.model

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jokku.jokeapp.data.Model
import kotlinx.coroutines.launch

class JokeViewModel(private val model: Model): ViewModel() {
    private var displayCallback: DisplayCallback? = null

    fun init(callback: DisplayCallback) {
        displayCallback = callback
    }

    fun getJoke() = viewModelScope.launch {
        val uiModel = model.getJoke()
        displayCallback?.let {
            uiModel.map(it)
        }
    }

    fun chooseFavorites(isFavorite: Boolean) {
        model.chooseDataSource(isFavorite)
    }

    fun changeJokeStatus() = viewModelScope.launch {
        val uiModel = model.changeJokeStatus()
        displayCallback?.let {
            uiModel?.map(it)
        }
    }

    fun clear() {
        displayCallback = null
    }
}

interface DisplayCallback {
    fun provideText(text: String)
    fun provideIconRes(@DrawableRes id: Int)
}