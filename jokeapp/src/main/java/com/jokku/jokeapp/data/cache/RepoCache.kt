package com.jokku.jokeapp.data.cache

import com.jokku.jokeapp.data.ItemStatusChanger
import com.jokku.jokeapp.data.RepoModel

interface RepoCache : ItemStatusChanger {
    fun save(data: RepoModel)
    fun clear()
}

class BaseRepoCache : RepoCache {
    private var cachedItem: ItemStatusChanger = ItemStatusChanger.Empty()

    override fun save(data: RepoModel) {
        cachedItem = data
    }

    override fun clear() {
        cachedItem = ItemStatusChanger.Empty()
    }

    override suspend fun changeItemStatus(statusChanger: StatusChanger): RepoModel {
        return cachedItem.changeItemStatus(statusChanger)
    }
}