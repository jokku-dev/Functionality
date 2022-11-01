package com.jokku.jokeapp

import android.app.Application
import com.jokku.jokeapp.data.BaseCachedJoke
import com.jokku.jokeapp.data.BaseModel
import com.jokku.jokeapp.data.CacheResultHandler
import com.jokku.jokeapp.data.CloudResultHandler
import com.jokku.jokeapp.data.source.BaseCacheDataSource
import com.jokku.jokeapp.data.source.BaseCloudDataSource
import com.jokku.jokeapp.data.source.JokeService
import com.jokku.jokeapp.model.*
import com.jokku.jokeapp.util.BaseRealmProvider
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
        val cachedJoke = BaseCachedJoke()
        val cacheDataSource = BaseCacheDataSource(BaseRealmProvider())
        val resourceManager = BaseResourceManager(this)
        mainViewModel = MainViewModel(
            BaseModel(
                cacheDataSource,
                CacheResultHandler(
                    cachedJoke,
                    cacheDataSource,
                    NoCachedJokes(resourceManager)
                ),
                CloudResultHandler(
                    cachedJoke,
                    BaseCloudDataSource(retrofit.create(JokeService::class.java)),
                    NoConnection(resourceManager),
                    ServiceUnavailable(resourceManager)
                ),
                cachedJoke
            ),
            BaseCommunicator()
        )
    }
}