package com.jokku.jokeapp

import android.app.Application

class JokeApplication : Application() {
    lateinit var viewModel: ViewModel

    override fun onCreate() {
        super.onCreate()
        viewModel = ViewModel(TestModel(BaseResourceManager(this)))
    }
}