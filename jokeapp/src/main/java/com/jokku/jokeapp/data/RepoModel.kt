package com.jokku.jokeapp.data

import com.jokku.jokeapp.core.ModelMapper
import com.jokku.jokeapp.data.cache.StatusChanger

class RepoModel(
    private val id: Int,
    private val firstText: String,
    private val secondText: String,
    private val cached: Boolean = false
) : ItemStatusChanger {

    override suspend fun changeItemStatus(statusChanger: StatusChanger): RepoModel {
        return statusChanger.addOrRemove(id, this)
    }
    fun <T> map(mapper: ModelMapper<T>): T {
        return mapper.map(id, firstText, secondText, cached)
    }

    fun changeCached(cached: Boolean): RepoModel {
        return RepoModel(id, firstText, secondText, cached)
    }
}