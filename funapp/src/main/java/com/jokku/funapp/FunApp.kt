package com.jokku.funapp

import android.app.Application
import com.jokku.funapp.data.cache.JokeRealmProvider
import com.jokku.funapp.data.cache.QuoteRealmProvider
import com.jokku.funapp.data.cache.RealmProvider
import com.jokku.funapp.domain.FailureFactory
import com.jokku.funapp.domain.FailureHandler
import com.jokku.funapp.presentation.JokesModule
import com.jokku.funapp.presentation.QuotesModule
import com.jokku.funapp.presentation.ViewModelFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FunApp : Application() {

    val viewModelFactory by lazy {
        ViewModelFactory(
            JokesModule(failureHandler, jokeRealmProvider, retrofit),
            QuotesModule(failureHandler, quoteRealmProvider, retrofit),
        )
    }
    private lateinit var retrofit: Retrofit
    private lateinit var jokeRealmProvider: RealmProvider
    private lateinit var quoteRealmProvider: RealmProvider
    private lateinit var failureHandler: FailureHandler

    override fun onCreate() {
        super.onCreate()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl("https://www.google.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        jokeRealmProvider = JokeRealmProvider()
        quoteRealmProvider = QuoteRealmProvider()
        failureHandler = FailureFactory(BaseResourceManager(this))
    }
}