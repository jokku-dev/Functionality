package com.jokku.jokeapp

import android.app.Application
import com.jokku.jokeapp.core.DataModelMapper
import com.jokku.jokeapp.core.RealmMapper
import com.jokku.jokeapp.core.SuccessMapper
import com.jokku.jokeapp.data.BaseCachedJoke
import com.jokku.jokeapp.data.BaseJokeRepository
import com.jokku.jokeapp.data.BaseRealmProvider
import com.jokku.jokeapp.data.source.BaseCacheDataSource
import com.jokku.jokeapp.data.source.NewJokeCloudDataSource
import com.jokku.jokeapp.data.source.NewJokeService
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
            NewJokeCloudDataSource(retrofit.create(NewJokeService::class.java))
        val resourceManager = BaseResourceManager(this)
        val repository = BaseJokeRepository(cacheDataSource, cloudDataSource, BaseCachedJoke())
        val interactor =
            BaseJokeInteractor(repository, JokeFailureFactory(resourceManager), SuccessMapper())

        mainViewModel = MainViewModel(interactor, BaseCommunicator())
    }
}