package com.jokku.jokeapp

import android.app.Application
import com.jokku.jokeapp.data.BaseModel
import com.jokku.jokeapp.data.entity.JokeRealm
import com.jokku.jokeapp.data.source.*
import com.jokku.jokeapp.model.ViewModel
import com.jokku.jokeapp.util.BaseResourceManager
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JokeApplication : Application() {
    lateinit var viewModel: ViewModel

    override fun onCreate() {
        super.onCreate()

        val config = RealmConfiguration.Builder(setOf(JokeRealm::class)).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.google.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        viewModel = ViewModel(
            BaseModel(
                BaseCacheDataSource(Realm.open(config)),
                BaseCloudDataSource(retrofit.create(JokeService::class.java)),
                BaseResourceManager(this)
            )
        )
    }
}