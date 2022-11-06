package com.jokku.jokeapp

import android.app.Application
import com.jokku.jokeapp.core.DataModelMapper
import com.jokku.jokeapp.core.RealmMapper
import com.jokku.jokeapp.core.SuccessMapper
import com.jokku.jokeapp.data.*
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
        val cacheDataSource =
            BaseCacheDataSource(BaseRealmProvider(), RealmMapper(), DataModelMapper())
        val cloudDataSource =
            BaseCloudDataSource(retrofit.create(JokeService::class.java), DataModelMapper())
        val resourceManager = BaseResourceManager(this)
        val repository = BaseJokeRepository(cacheDataSource, cloudDataSource, BaseCachedJoke())
        val interactor =
            BaseJokeInteractor(repository, JokeFailureFactory(resourceManager), SuccessMapper())

        mainViewModel = MainViewModel(interactor, BaseCommunicator())
    }
}