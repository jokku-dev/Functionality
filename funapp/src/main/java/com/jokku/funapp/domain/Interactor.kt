package com.jokku.funapp.domain

import com.jokku.funapp.core.FromRepoMapper
import com.jokku.funapp.data.Repository

interface Interactor {
    suspend fun getItem(): DomainItem
    suspend fun changePreference(): DomainItem
    fun chooseFavorites(favorites: Boolean)
}

class BaseInteractor<E>(
    private val repository: Repository<E>,
    private val failureHandler: FailureHandler,
    private val mapper: FromRepoMapper<DomainItem.Success, E>
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