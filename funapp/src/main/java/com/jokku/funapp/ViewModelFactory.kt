package com.jokku.funapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jokku.funapp.presentation.MainViewModel
import com.jokku.funapp.presentation.fragment.JokesViewModel
import com.jokku.funapp.presentation.fragment.QuotesViewModel

class ViewModelFactory(
    private val activityModule: ActivityModule,
    private val jokesModule: JokesModule,
    private val quotesModule: QuotesModule
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val module = when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> activityModule
            modelClass.isAssignableFrom(JokesViewModel::class.java) -> jokesModule
            modelClass.isAssignableFrom(QuotesViewModel::class.java) -> quotesModule
            else -> throw IllegalStateException("unknown type of viewModel")
        }
        return module.getViewModel() as T
    }
}