package com.jokku.jokeapp

import android.app.Application
import com.jokku.jokeapp.data.BaseCachedJoke
import com.jokku.jokeapp.data.BaseJokeRepository
import com.jokku.jokeapp.data.BaseRealmProvider
import com.jokku.jokeapp.data.source.BaseCacheDataSource
import com.jokku.jokeapp.data.source.BaseCloudDataSource
import com.jokku.jokeapp.data.source.JokeService
import com.jokku.jokeapp.domain.BaseJokeInteractor
import com.jokku.jokeapp.domain.JokeFailureFactory
import com.jokku.jokeapp.presentation.model.BaseCommunicator
import com.jokku.jokeapp.presentation.model.MainViewModel
import com.jokku.jokeapp.util.BaseResourceManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JokeApplication : Application() {
    lateinit var mainViewModel: MainViewModel

    override fun onCreate() {
        super.onCreate()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.google.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val cacheDataSource = BaseCacheDataSource(BaseRealmProvider())
        val resourceManager = BaseResourceManager(this)
        val cloudDataSource = BaseCloudDataSource(retrofit.create(JokeService::class.java))
        val repository = BaseJokeRepository(cacheDataSource, cloudDataSource, BaseCachedJoke())
        val interactor = BaseJokeInteractor(repository, JokeFailureFactory(resourceManager))

        mainViewModel = MainViewModel(interactor, BaseCommunicator())
    }
}