package com.jokku.funapp

import android.app.Application
import com.jokku.funapp.core.*
import com.jokku.funapp.data.BaseRepository
import com.jokku.funapp.data.cache.*
import com.jokku.funapp.data.cloud.NewJokeCloudDataSource
import com.jokku.funapp.data.cloud.NewJokeService
import com.jokku.funapp.data.cloud.QuoteCloudDataSource
import com.jokku.funapp.data.cloud.QuoteService
import com.jokku.funapp.domain.BaseInteractor
import com.jokku.funapp.domain.FailureFactory
import com.jokku.funapp.presentation.BaseCommunicator
import com.jokku.funapp.presentation.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FunApplication : Application() {
    lateinit var mainViewModel: MainViewModel
    lateinit var quoteViewModel: MainViewModel

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
        val resourceManager = BaseResourceManager(this)
        val failureHandler = FailureFactory(resourceManager)
        val jokeSuccessMapper = DomainSuccessMapper<Int>()
        val quoteSuccessMapper = DomainSuccessMapper<String>()
        val jokeRepoCache = BaseRepoCache<Int>()
        val quoteRepoCache = BaseRepoCache<String>()

        val jokeCacheDataSource =
            JokeCacheDataSource(JokeRealmProvider(), JokeRealmMapper(), JokeRealmToRepoMapper())
        val quoteCacheDataSource =
            QuoteCacheDataSource(QuoteRealmProvider(), QuoteRealmMapper(), QuoteRealmToRepoMapper())

        val jokeCloudDataSource = NewJokeCloudDataSource(retrofit.create(NewJokeService::class.java))
        val quoteCloudDataSource = QuoteCloudDataSource(retrofit.create(QuoteService::class.java))

        val jokeRepository =
            BaseRepository(jokeCacheDataSource, jokeCloudDataSource, jokeRepoCache)
        val quoteRepository =
            BaseRepository(quoteCacheDataSource, quoteCloudDataSource, quoteRepoCache)

        val jokeInteractor = BaseInteractor(jokeRepository, failureHandler, jokeSuccessMapper)
        val quoteInteractor = BaseInteractor(quoteRepository, failureHandler, quoteSuccessMapper)

        mainViewModel = MainViewModel(jokeInteractor, BaseCommunicator())
        quoteViewModel = MainViewModel(quoteInteractor, BaseCommunicator())
    }
}