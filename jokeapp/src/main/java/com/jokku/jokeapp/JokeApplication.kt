package com.jokku.jokeapp

import android.app.Application
import com.jokku.jokeapp.core.DataModelMapper
import com.jokku.jokeapp.core.RealmMapper
import com.jokku.jokeapp.core.SuccessMapper
import com.jokku.jokeapp.data.cache.BaseCachedJoke
import com.jokku.jokeapp.data.BaseJokeRepository
import com.jokku.jokeapp.data.cache.BaseRealmProvider
import com.jokku.jokeapp.data.cache.BaseCacheDataSource
import com.jokku.jokeapp.data.cloud.NewJokeCloudDataSource
import com.jokku.jokeapp.data.cloud.NewJokeService
import com.jokku.jokeapp.domain.BaseJokeInteractor
import com.jokku.jokeapp.domain.JokeFailureFactory
import com.jokku.jokeapp.presentation.BaseCommunicator
import com.jokku.jokeapp.presentation.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JokeApplication : Application() {
    lateinit var mainViewModel: MainViewModel

    override fun onCreate() {
        super.onCreate()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.google.com")
            .client(client)
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