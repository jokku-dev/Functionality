package com.jokku.jokeapp.data

import com.jokku.jokeapp.data.cache.StatusChanger

interface ItemStatusChanger {
    suspend fun changeItemStatus(statusChanger: StatusChanger): RepoModel

    class Empty : ItemStatusChanger {
        override suspend fun changeItemStatus(statusChanger: StatusChanger): RepoModel {
            throw IllegalStateException("empty data called")
        }
    }
}