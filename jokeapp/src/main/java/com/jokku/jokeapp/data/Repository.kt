package com.jokku.jokeapp.data

import com.jokku.jokeapp.data.cache.CacheDataSource
import com.jokku.jokeapp.data.cache.RepoCache
import com.jokku.jokeapp.data.cloud.CloudDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface Repository {
    suspend fun getCommonItem(): RepoModel
    suspend fun changeItemStatus(): RepoModel
    fun chooseDataSource(cached: Boolean)
}

class BaseRepository(
    private val cacheDataSource: CacheDataSource,
    private val cloudDataSource: CloudDataSource,
    private val repoCache: RepoCache
) : Repository {
    private var currentDataSource: DataFetcher = cloudDataSource

    override suspend fun getCommonItem(): RepoModel = withContext(Dispatchers.IO) {
        try {
            val data = currentDataSource.getData()
            repoCache.save(data)
            data
        } catch (e: Exception) {
            repoCache.clear()
            throw e
        }
    }

    override suspend fun changeItemStatus(): RepoModel = repoCache.changeItemStatus(cacheDataSource)

    override fun chooseDataSource(cached: Boolean) {
        currentDataSource = if (cached) cacheDataSource else cloudDataSource
    }
}