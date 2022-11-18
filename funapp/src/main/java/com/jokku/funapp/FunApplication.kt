package com.jokku.funapp

import android.app.Application
import com.jokku.funapp.core.*
import com.jokku.funapp.data.BaseFunRepository
import com.jokku.funapp.data.cache.*
import com.jokku.funapp.data.cloud.NewJokeCloudDataSource
import com.jokku.funapp.data.cloud.NewJokeService
import com.jokku.funapp.data.cloud.QuoteCloudDataSource
import com.jokku.funapp.data.cloud.QuoteService
import com.jokku.funapp.domain.BaseFunInteractor
import com.jokku.funapp.domain.FailureFactory
import com.jokku.funapp.presentation.BaseCommunicator
import com.jokku.funapp.presentation.BaseFunViewModel
import com.jokku.funapp.presentation.Communicator
import com.jokku.funapp.presentation.FunViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FunApplication : Application() {
    lateinit var jokeViewModel: FunViewModel<Int>
    lateinit var quoteViewModel: FunViewModel<String>
    lateinit var jokeCommunicator: Communicator<Int>
    lateinit var quoteCommunicator: Communicator<String>

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
            BaseFunRepository(jokeCacheDataSource, jokeCloudDataSource, jokeRepoCache)
        val quoteRepository =
            BaseFunRepository(quoteCacheDataSource, quoteCloudDataSource, quoteRepoCache)

        val jokeInteractor = BaseFunInteractor(jokeRepository, failureHandler, jokeSuccessMapper)
        val quoteInteractor = BaseFunInteractor(quoteRepository, failureHandler, quoteSuccessMapper)

        jokeCommunicator = BaseCommunicator()
        quoteCommunicator = BaseCommunicator()

        jokeViewModel = BaseFunViewModel(jokeInteractor, jokeCommunicator)
        quoteViewModel = BaseFunViewModel(quoteInteractor, quoteCommunicator)
    }
}