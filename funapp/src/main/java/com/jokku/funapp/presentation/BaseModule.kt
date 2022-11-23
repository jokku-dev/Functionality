package com.jokku.funapp.presentation

import com.jokku.funapp.core.*
import com.jokku.funapp.data.BaseRepository
import com.jokku.funapp.data.cache.BaseRepoCache
import com.jokku.funapp.data.cache.JokeCacheDataSource
import com.jokku.funapp.data.cache.QuoteCacheDataSource
import com.jokku.funapp.data.cache.RealmProvider
import com.jokku.funapp.data.cloud.NewJokeCloudDataSource
import com.jokku.funapp.data.cloud.NewJokeService
import com.jokku.funapp.data.cloud.QuoteCloudDataSource
import com.jokku.funapp.data.cloud.QuoteService
import com.jokku.funapp.domain.BaseInteractor
import com.jokku.funapp.domain.FailureHandler
import retrofit2.Retrofit

abstract class BaseModule<E, T : MainViewModel<E>> {
    abstract fun getViewModel(): T
    abstract fun getCommunicator(): Communicator<E>
}

class JokesModule(
    private val failureHandler: FailureHandler,
    private val realmProvider: RealmProvider,
    private val retrofit: Retrofit
) : BaseModule<Int, JokesViewModel>() {
    private var communicator: Communicator<Int>? = null

    override fun getViewModel() = JokesViewModel(getInteractor(), getCommunicator())

    override fun getCommunicator(): Communicator<Int> {
        if (communicator == null) communicator = BaseCommunicator()
        return communicator!!
    }

    private fun getInteractor() =
        BaseInteractor(getRepository(), failureHandler, DomainSuccessMapper())
    private fun getRepository() =
        BaseRepository(getCacheDataSource(), getCloudDataSource(), BaseRepoCache())
    private fun getCacheDataSource() =
        JokeCacheDataSource(realmProvider, JokeRealmMapper(), JokeRealmToRepoMapper())
    private fun getCloudDataSource() =
        NewJokeCloudDataSource(retrofit.create(NewJokeService::class.java))
}

class QuotesModule(
    private val failureHandler: FailureHandler,
    private val realmProvider: RealmProvider,
    private val retrofit: Retrofit
) : BaseModule<String, QuotesViewModel>() {
    private var communicator: Communicator<String>? = null

    override fun getViewModel() = QuotesViewModel(getInteractor(), getCommunicator())

    override fun getCommunicator(): Communicator<String> {
        if (communicator == null) communicator = BaseCommunicator()
        return communicator!!
    }

    private fun getInteractor() =
        BaseInteractor(getRepository(), failureHandler, DomainSuccessMapper())
    private fun getRepository() =
        BaseRepository(getCacheDataSource(), getCloudDataSource(), BaseRepoCache())
    private fun getCacheDataSource() =
        QuoteCacheDataSource(realmProvider, QuoteRealmMapper(), QuoteRealmToRepoMapper())
    private fun getCloudDataSource() =
        QuoteCloudDataSource(retrofit.create(QuoteService::class.java))
}