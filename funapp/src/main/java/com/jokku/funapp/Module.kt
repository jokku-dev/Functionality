package com.jokku.funapp

import androidx.lifecycle.ViewModel
import com.jokku.funapp.core.*
import com.jokku.funapp.data.BaseRepository
import com.jokku.funapp.data.cache.*
import com.jokku.funapp.data.cloud.NewJokeCloudDataSource
import com.jokku.funapp.data.cloud.NewJokeService
import com.jokku.funapp.data.cloud.QuoteCloudDataSource
import com.jokku.funapp.data.cloud.QuoteService
import com.jokku.funapp.domain.BaseInteractor
import com.jokku.funapp.domain.FailureHandler
import com.jokku.funapp.presentation.MainViewModel
import com.jokku.funapp.presentation.NavigationCommunicator
import com.jokku.funapp.presentation.ScreenPosition
import com.jokku.funapp.presentation.fragment.*
import retrofit2.Retrofit

interface Module<T : ViewModel> {
    fun getViewModel(): T

    abstract class Base<E, T : BaseViewModel<E>> : Module<T> {
        protected abstract fun getCommunicator(): Communicator<E>
    }
}

class MainModule(private val persistentDataSource: PersistentDataSource) : Module<MainViewModel> {
    override fun getViewModel() = MainViewModel(
        ScreenPosition.Base(persistentDataSource),
        NavigationCommunicator.Base()
    )
}

class JokesModule(
    private val failureHandler: FailureHandler,
    private val realmProvider: RealmProvider,
    private val retrofit: Retrofit
) : Module.Base<Int, JokesViewModel>() {
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
) : Module.Base<String, QuotesViewModel>() {
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