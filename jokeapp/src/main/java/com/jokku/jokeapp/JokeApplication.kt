package com.jokku.jokeapp

import android.app.Application
import com.jokku.jokeapp.data.BaseModel
import com.jokku.jokeapp.data.source.BaseCacheDataSource
import com.jokku.jokeapp.data.source.BaseCloudDataSource
import com.jokku.jokeapp.data.source.BaseRealmProvider
import com.jokku.jokeapp.data.source.JokeService
import com.jokku.jokeapp.model.JokeViewModel
import com.jokku.jokeapp.util.BaseResourceManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JokeApplication : Application() {
    lateinit var jokeViewModel: JokeViewModel

    override fun onCreate() {
        super.onCreate()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.google.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        jokeViewModel = JokeViewModel(
            BaseModel(
                BaseCacheDataSource(BaseRealmProvider()),
                BaseCloudDataSource(retrofit.create(JokeService::class.java)),
                BaseResourceManager(this)
            )
        )
    }
}