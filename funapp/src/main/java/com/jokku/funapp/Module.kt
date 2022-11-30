package com.jokku.funapp

import androidx.lifecycle.ViewModel
import com.jokku.funapp.core.*
import com.jokku.funapp.data.BaseRepository
import com.jokku.funapp.data.cache.BaseRepoCache
import com.jokku.funapp.data.cache.JokeCacheDataSource
import com.jokku.funapp.data.cache.QuoteCacheDataSource
import com.jokku.funapp.data.cloud.NewJokeCloudDataSource
import com.jokku.funapp.data.cloud.NewJokeService
import com.jokku.funapp.data.cloud.QuoteCloudDataSource
import com.jokku.funapp.data.cloud.QuoteService
import com.jokku.funapp.data.cloud.mock.MockJokeCloudDataSource
import com.jokku.funapp.data.cloud.mock.MockQuoteCloudDataSource
import com.jokku.funapp.domain.BaseInteractor
import com.jokku.funapp.presentation.MainViewModel
import com.jokku.funapp.presentation.NavigationCommunicator
import com.jokku.funapp.presentation.ScreenPosition
import com.jokku.funapp.presentation.fragment.BaseCommunicator
import com.jokku.funapp.presentation.fragment.BaseViewModel
import com.jokku.funapp.presentation.fragment.JokesViewModel
import com.jokku.funapp.presentation.fragment.QuotesViewModel

interface Module<T : ViewModel> {
    fun getViewModel(): T

    abstract class Type<E, T : BaseViewModel<E>> : Module<T>
}

class ActivityModule(private val instancesProvider: InstancesProvider) : Module<MainViewModel> {
    override fun getViewModel() = MainViewModel(
        ScreenPosition.Base(instancesProvider.providePersistentDataSource()),
        NavigationCommunicator.Base()
    )
}

class JokesModule(
    private val instancesProvider: InstancesProvider,
    private val useMocks: Boolean
) : Module.Type<Int, JokesViewModel>() {
    override fun getViewModel() = JokesViewModel(getInteractor(), BaseCommunicator())
    private fun getInteractor() = BaseInteractor(
        getRepository(),
        instancesProvider.provideFailureHandler(),
        DomainSuccessMapper()
    )

    private fun getRepository() =
        BaseRepository(getCacheDataSource(), getCloudDataSource(), BaseRepoCache())

    private fun getCacheDataSource() =
        JokeCacheDataSource(instancesProvider, JokeRealmMapper(), JokeRealmToRepoMapper())

    private fun getCloudDataSource() =
        if (useMocks) MockJokeCloudDataSource()
        else NewJokeCloudDataSource(instancesProvider.makeService(NewJokeService::class.java))
}

class QuotesModule(
    private val instancesProvider: InstancesProvider,
    private val useMocks: Boolean
) : Module.Type<String, QuotesViewModel>() {
    override fun getViewModel() = QuotesViewModel(getInteractor(), BaseCommunicator())
    private fun getInteractor() = BaseInteractor(
        getRepository(),
        instancesProvider.provideFailureHandler(),
        DomainSuccessMapper()
    )

    private fun getRepository() =
        BaseRepository(getCacheDataSource(), getCloudDataSource(), BaseRepoCache())

    private fun getCacheDataSource() =
        QuoteCacheDataSource(instancesProvider, QuoteRealmMapper(), QuoteRealmToRepoMapper())

    private fun getCloudDataSource() =
        if (useMocks) MockQuoteCloudDataSource()
        else QuoteCloudDataSource(instancesProvider.makeService(QuoteService::class.java))


}