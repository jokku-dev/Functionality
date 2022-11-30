package com.jokku.funapp.domain

import com.jokku.funapp.core.FromRepoMapper
import com.jokku.funapp.data.Repository

interface Interactor<E> {
    suspend fun getItem(): DomainItem<E>
    suspend fun getItemList(): List<DomainItem<E>>
    suspend fun changeIsFavorite(): DomainItem<E>
    suspend fun removeItem(id: E)
    fun getFavorites(favorites: Boolean)
}

class BaseInteractor<E>(
    private val repository: Repository<E>,
    private val failureHandler: FailureHandler,
    private val mapper: FromRepoMapper<DomainItem.Success<E>, E>
) : Interactor<E> {

    override suspend fun getItem(): DomainItem<E> {
        return try {
            repository.getFunItem().map(mapper)
        } catch (e: Exception) {
            DomainItem.Failed(failureHandler.handle(e))
        }
    }

    override suspend fun getItemList(): List<DomainItem<E>> {
        return try {
            repository.getFunItemList().map { it.map(mapper) }
        } catch (e: Exception) {
            listOf(DomainItem.Failed(failureHandler.handle(e)))
        }
    }

    override suspend fun changeIsFavorite(): DomainItem<E> {
        return try {
            repository.changeItemStatus().map(mapper)
        } catch (e: Exception) {
            DomainItem.Failed(failureHandler.handle(e))
        }
    }

    override suspend fun removeItem(id: E) {
        repository.removeItem(id)
    }

    override fun getFavorites(favorites: Boolean) {
        repository.chooseDataSource(favorites)
    }


}