package com.jokku.funapp

import android.app.Application
import com.jokku.funapp.data.cache.BaseRealmProvider
import com.jokku.funapp.data.cache.PersistentDataSource
import com.jokku.funapp.data.cache.RealmProvider
import com.jokku.funapp.domain.FailureFactory
import com.jokku.funapp.domain.FailureHandler
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FunApp : Application() {

    val viewModelFactory by lazy {
        ViewModelFactory(
            MainModule(persistentDataSource),
            JokesModule(failureHandler, realmProvider, retrofit, useMocks),
            QuotesModule(failureHandler, realmProvider, retrofit, useMocks)
        )
    }
    private val useMocks = false
    private lateinit var retrofit: Retrofit
    private lateinit var realmProvider: RealmProvider
    private lateinit var failureHandler: FailureHandler
    private lateinit var persistentDataSource: PersistentDataSource

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
        realmProvider = BaseRealmProvider(useMocks)
        failureHandler = FailureFactory(BaseResourceManager(this))
        persistentDataSource = PersistentDataSource.Base(this)
    }
}