package com.jokku.funapp

import android.content.Context
import com.jokku.funapp.data.cache.BaseRealmProvider
import com.jokku.funapp.data.cache.PersistentDataSource
import com.jokku.funapp.data.cache.RealmProvider
import com.jokku.funapp.domain.FailureFactory
import com.jokku.funapp.domain.FailureHandler
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface InstancesProvider : RealmProvider {
    fun <T> makeService(service: Class<T>): T
    fun providePersistentDataSource(): PersistentDataSource
    fun provideFailureHandler(): FailureHandler
}

class CoreModule(context: Context, useMocks: Boolean) : InstancesProvider {
    private val realmProvider by lazy { BaseRealmProvider(useMocks) }
    private val failureHandler by lazy { FailureFactory(BaseResourceManager(context)) }
    private val persistentDataSource by lazy { PersistentDataSource.Base(context) }
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.google.com")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }).build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun <T> makeService(service: Class<T>): T = retrofit.create(service)
    override fun providePersistentDataSource() = persistentDataSource
    override fun provideFailureHandler() = failureHandler
    override fun provideRealm() = realmProvider.provideRealm()
}