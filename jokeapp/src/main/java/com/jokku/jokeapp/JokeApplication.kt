package com.jokku.jokeapp

import android.app.Application
import com.jokku.jokeapp.data.BaseModel
import com.jokku.jokeapp.data.source.BaseCloudDataSource
import com.jokku.jokeapp.data.source.JokeService
import com.jokku.jokeapp.data.source.TestCacheDataSource
import com.jokku.jokeapp.data.source.TestCloudDataSource
import com.jokku.jokeapp.model.ViewModel
import com.jokku.jokeapp.util.BaseResourceManager
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

        viewModel = ViewModel(
            BaseModel(
                TestCacheDataSource(),
                BaseCloudDataSource(retrofit.create(JokeService::class.java)),
                BaseResourceManager(this)
            )
        )
        /*viewModel = ViewModel(
            BaseModel(
                TestCacheDataSource(),
                TestCloudDataSource(),
                BaseResourceManager(this)
            )
        )*/
    }
}