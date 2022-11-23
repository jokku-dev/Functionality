package com.jokku.funapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(
    private val jokesModule: JokesModule,
    private val quotesModule: QuotesModule
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val module = when {
            modelClass.isAssignableFrom(JokesViewModel::class.java) -> jokesModule
            modelClass.isAssignableFrom(QuotesViewModel::class.java) -> quotesModule
            else -> throw IllegalStateException("unknown type of viewModel")
        }
        return module.getViewModel() as T
    }
}