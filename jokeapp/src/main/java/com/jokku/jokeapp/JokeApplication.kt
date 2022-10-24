package com.jokku.jokeapp

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JokeApplication : Application() {
    lateinit var viewModel: ViewModel

    override fun onCreate() {
        super.onCreate()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.google.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        /*viewModel = ViewModel(
            BaseModel(
                TestCacheDataSource(), BaseCloudDataSource(), BaseResourceManager(this)
            )
        )*/
        viewModel = ViewModel(
            BaseModel(
                TestCacheDataSource(), TestCloudDataSource(), BaseResourceManager(this)
            )
        )
    }
}