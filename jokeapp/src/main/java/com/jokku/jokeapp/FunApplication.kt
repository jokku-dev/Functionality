package com.jokku.jokeapp

import android.app.Application
import com.jokku.jokeapp.core.JokeRealmMapper
import com.jokku.jokeapp.core.QuoteRealmMapper
import com.jokku.jokeapp.core.SuccessMapper
import com.jokku.jokeapp.data.BaseRepository
import com.jokku.jokeapp.data.cache.BaseRealmProvider
import com.jokku.jokeapp.data.cache.BaseRepoCache
import com.jokku.jokeapp.data.cache.JokeCacheDataSource
import com.jokku.jokeapp.data.cache.QuoteCacheDataSource
import com.jokku.jokeapp.data.cloud.NewJokeCloudDataSource
import com.jokku.jokeapp.data.cloud.NewJokeService
import com.jokku.jokeapp.data.cloud.QuoteCloudDataSource
import com.jokku.jokeapp.data.cloud.QuoteService
import com.jokku.jokeapp.domain.BaseInteractor
import com.jokku.jokeapp.domain.FailureFactory
import com.jokku.jokeapp.presentation.BaseCommunicator
import com.jokku.jokeapp.presentation.MainViewModel
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
        val realm = BaseRealmProvider()
        val resourceManager = BaseResourceManager(this)
        val mapper = SuccessMapper()
        val failureHandler = FailureFactory(resourceManager)
        val repoCache = BaseRepoCache()

        val jokeCacheDataSource = JokeCacheDataSource(realm, JokeRealmMapper())
        val quoteCacheDataSource = QuoteCacheDataSource(realm, QuoteRealmMapper())

        val jokeCloudDataSource = NewJokeCloudDataSource(retrofit.create(NewJokeService::class.java))
        val quoteCloudDataSource = QuoteCloudDataSource(retrofit.create(QuoteService::class.java))

        val jokeRepository = BaseRepository(jokeCacheDataSource, jokeCloudDataSource, repoCache)
        val quoteRepository = BaseRepository(quoteCacheDataSource, quoteCloudDataSource, repoCache)

        val jokeInteractor = BaseInteractor(jokeRepository, failureHandler, mapper)
        val quoteInteractor = BaseInteractor(quoteRepository, failureHandler, mapper)

        mainViewModel = MainViewModel(jokeInteractor, BaseCommunicator())
        quoteViewModel = MainViewModel(quoteInteractor, BaseCommunicator())
    }
}