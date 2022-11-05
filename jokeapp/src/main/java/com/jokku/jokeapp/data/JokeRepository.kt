package com.jokku.jokeapp.data

import com.jokku.jokeapp.data.entity.JokeDataModel
import com.jokku.jokeapp.data.source.CacheDataSource
import com.jokku.jokeapp.data.source.CloudDataSource
import com.jokku.jokeapp.data.source.JokeDataFetcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface JokeRepository {
    suspend fun getJoke(): JokeDataModel
    suspend fun changeJokeStatus(): JokeDataModel
    fun chooseDataSource(cached: Boolean)
}

class BaseJokeRepository(
    private val cacheDataSource: CacheDataSource,
    private val cloudDataSource: CloudDataSource,
    private val cachedJoke: CachedJoke
) : JokeRepository {
    private var currentDataSource: JokeDataFetcher = cloudDataSource

    override suspend fun getJoke(): JokeDataModel = withContext(Dispatchers.IO) {
        try {
            val joke = currentDataSource.getJoke()
            cachedJoke.saveJoke(joke)
            joke
        } catch (e: Exception) {
            cachedJoke.clear()
            throw e
        }
    }

    override suspend fun changeJokeStatus(): JokeDataModel = cachedJoke.changeModelType(cacheDataSource)

    override fun chooseDataSource(cached: Boolean) {
        currentDataSource = if (cached) cacheDataSource else cloudDataSource
    }
}