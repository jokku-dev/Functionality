package com.jokku.funapp.data

import com.jokku.funapp.data.cache.StatusChanger

interface ItemStatusChanger<E> {
    suspend fun changeItemStatus(statusChanger: StatusChanger<E>): RepoModel<E>

    class Empty<E> : ItemStatusChanger<E> {
        override suspend fun changeItemStatus(statusChanger: StatusChanger<E>): RepoModel<E> {
            throw IllegalStateException("empty data called")
        }
    }
}