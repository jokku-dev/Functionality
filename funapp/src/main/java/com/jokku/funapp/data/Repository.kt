package com.jokku.funapp.data

import com.jokku.funapp.data.cache.CacheDataSource
import com.jokku.funapp.data.cache.RepoCache
import com.jokku.funapp.data.cloud.CloudDataSource
import com.jokku.funapp.domain.NoCachedDataException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface Repository<E> {
    suspend fun getFunItem(): RepoModel<E>
    suspend fun getFunItemList(): List<RepoModel<E>>
    suspend fun changeItemStatus(): RepoModel<E>
    suspend fun removeItem(id: E)
    fun chooseDataSource(cached: Boolean)
}

class BaseRepository<E>(
    private val cacheDataSource: CacheDataSource<E>,
    private val cloudDataSource: CloudDataSource<E>,
    private val repoCache: RepoCache<E>
) : Repository<E> {
    private var currentDataSource: DataFetcher<E> = cloudDataSource

    override fun chooseDataSource(cached: Boolean) {
        currentDataSource = if (cached) cacheDataSource else cloudDataSource
    }

    override suspend fun getFunItem(): RepoModel<E> = withContext(Dispatchers.IO) {
        val data = currentDataSource.getData()
        repoCache.save(data)
        if (data.isEmpty()) {
            repoCache.clear()
            throw NoCachedDataException()
        } else data


    }

    override suspend fun getFunItemList(): List<RepoModel<E>> = withContext(Dispatchers.IO) {
        cacheDataSource.getDataList().ifEmpty { throw NoCachedDataException() }
    }

    override suspend fun changeItemStatus(): RepoModel<E> = withContext(Dispatchers.IO) {
        repoCache.changeItemStatus(cacheDataSource)
    }

    override suspend fun removeItem(id: E) {
        cacheDataSource.remove(id)
    }
}