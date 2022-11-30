package com.jokku.funapp.data.cache

import com.jokku.funapp.data.ItemStatusChanger
import com.jokku.funapp.data.RepoModel

interface RepoCache<E> : ItemStatusChanger<E> {
    fun save(data: RepoModel<E>)
    fun clear()
}

class BaseRepoCache<E> : RepoCache<E> {
    private var cachedItem: ItemStatusChanger<E> = ItemStatusChanger.Empty()

    override fun save(data: RepoModel<E>) {
        cachedItem = data
    }

    override fun clear() {
        cachedItem = ItemStatusChanger.Empty()
    }

    override suspend fun changeItemStatus(statusChanger: StatusChanger<E>): RepoModel<E> {
        return cachedItem.changeItemStatus(statusChanger)
    }
}