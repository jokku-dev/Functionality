package com.jokku.jokeapp.data

import com.jokku.jokeapp.data.source.CacheDataSource
import com.jokku.jokeapp.model.JokeUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface Model {
    suspend fun getJoke(): JokeUiModel
    suspend fun changeJokeStatus(): JokeUiModel?
    fun chooseDataSource(isCached: Boolean)
}

class BaseModel(
    private val cacheDataSource: CacheDataSource,
    private val cacheResultHandler: CacheResultHandler,
    private val cloudResultHandler: CloudResultHandler,
    private val cachedJoke: CachedJoke
) : Model {
    private var currentResultHandler: BaseResultHandler<*, *> = cloudResultHandler

    override suspend fun getJoke(): JokeUiModel = withContext(Dispatchers.IO) {
        currentResultHandler.process()
    }

    override suspend fun changeJokeStatus(): JokeUiModel? = cachedJoke.change(cacheDataSource)

    override fun chooseDataSource(isCached: Boolean) {
        currentResultHandler = if (isCached) cacheResultHandler else cloudResultHandler
    }
}