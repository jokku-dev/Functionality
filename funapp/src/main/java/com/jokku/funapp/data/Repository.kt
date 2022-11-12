package com.jokku.funapp.data

import com.jokku.funapp.data.cache.CacheDataSource
import com.jokku.funapp.data.cache.RepoCache
import com.jokku.funapp.data.cloud.CloudDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface Repository<E> {
    suspend fun getCommonItem(): RepoModel<E>
    suspend fun changeItemStatus(): RepoModel<E>
    fun chooseDataSource(cached: Boolean)
}

class BaseRepository<E>(
    private val cacheDataSource: CacheDataSource<E>,
    private val cloudDataSource: CloudDataSource<E>,
    private val repoCache: RepoCache<E>
) : Repository<E> {
    private var currentDataSource: DataFetcher<E> = cloudDataSource

    override suspend fun getCommonItem(): RepoModel<E> = withContext(Dispatchers.IO) {
        try {
            val data = currentDataSource.getData()
            repoCache.save(data)
            data
        } catch (e: Exception) {
            repoCache.clear()
            throw e
        }
    }

    override suspend fun changeItemStatus(): RepoModel<E> = repoCache.changeItemStatus(cacheDataSource)

    override fun chooseDataSource(cached: Boolean) {
        currentDataSource = if (cached) cacheDataSource else cloudDataSource
    }
}