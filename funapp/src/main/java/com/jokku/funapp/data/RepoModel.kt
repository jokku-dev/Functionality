package com.jokku.funapp.data

import com.jokku.funapp.core.FromRepoMapper
import com.jokku.funapp.data.cache.StatusChanger

class RepoModel<E>(
    private val id: E,
    private val firstText: String,
    private val secondText: String,
    private val cached: Boolean = false
) : ItemStatusChanger<E> {

    override suspend fun changeItemStatus(statusChanger: StatusChanger<E>): RepoModel<E> {
        return statusChanger.addOrRemove(id, this)
    }

    fun <T> map(mapper: FromRepoMapper<T, E>): T {
        return mapper.map(id, firstText, secondText, cached)
    }

    fun changeCached(cached: Boolean): RepoModel<E> {
        return RepoModel(id, firstText, secondText, cached)
    }

    fun isEmpty() = this.id == -1
}