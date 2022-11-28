package com.jokku.funapp

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

class FunApp : Application() {

    val viewModelFactory by lazy {
        ViewModelFactory(
            ActivityModule(coreModule),
            JokesModule(coreModule, useMocks),
            QuotesModule(coreModule, useMocks)
        )
    }
    private val useMocks = BuildConfig.DEBUG
    private lateinit var coreModule: CoreModule

    override fun onCreate() {
        super.onCreate()
        coreModule = CoreModule(this, useMocks)
    }

    fun <T: ViewModel> getViewModel(modelClass: Class<T>, owner: ViewModelStoreOwner): T =
        ViewModelProvider(owner, viewModelFactory)[modelClass]
}