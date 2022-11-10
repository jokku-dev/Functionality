package com.jokku.jokeapp.domain

import com.jokku.jokeapp.core.ModelMapper
import com.jokku.jokeapp.data.Repository

interface Interactor {
    suspend fun getItem(): DomainItem
    suspend fun changePreference(): DomainItem
    fun chooseFavorites(favorites: Boolean)
}

class BaseInteractor(
    private val repository: Repository,
    private val failureHandler: FailureHandler,
    private val mapper: ModelMapper<DomainItem.Success>
) : Interactor {

    override suspend fun getItem(): DomainItem {
        return try {
            repository.getCommonItem().map(mapper)
        } catch (e: Exception) {
            DomainItem.Failed(failureHandler.handle(e))
        }
    }

    override suspend fun changePreference(): DomainItem {
        return try {
            repository.changeItemStatus().map(mapper)
        } catch (e: Exception) {
            DomainItem.Failed(failureHandler.handle(e))
        }
    }

    override fun chooseFavorites(favorites: Boolean) {
        repository.chooseDataSource(favorites)
    }
}