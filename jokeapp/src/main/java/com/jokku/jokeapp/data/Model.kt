package com.jokku.jokeapp.data

import com.jokku.jokeapp.data.entity.JokeServerModel
import com.jokku.jokeapp.data.source.CacheDataSource
import com.jokku.jokeapp.data.source.CloudDataSource
import com.jokku.jokeapp.data.source.ErrorType
import com.jokku.jokeapp.data.source.Result
import com.jokku.jokeapp.model.*
import com.jokku.jokeapp.util.ResourceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface Model {
    fun init(callback: JokeCallback)
    suspend fun getJoke(): JokeUiModel
    suspend fun changeJokeStatus(): JokeUiModel?
    fun chooseDataSource(isFavorite: Boolean)
    fun clear()
}

class BaseModel(
    private val cacheDataSource: CacheDataSource,
    private val cloudDataSource: CloudDataSource,
    private val resourceManager: ResourceManager
) : Model {
    private val noConnection by lazy { NoConnection(resourceManager) }
    private val serviceUnavailable by lazy { ServiceUnavailable(resourceManager) }
    private val noCachedJokes by lazy { NoCachedJokes(resourceManager) }
    private var jokeCallback: JokeCallback? = null
    private var cachedJoke: Joke? = null
    private var getJokeFromCache = false

    override fun init(callback: JokeCallback) {
        jokeCallback = callback
    }

    override suspend fun getJoke(): JokeUiModel {
        if (getJokeFromCache) {
            return when (val result = cacheDataSource.getJoke()) {
                is Result.Success<Joke> -> result.data.let {
                    cachedJoke = it
                    it.toFavoriteJoke()
                }
                is Result.Error -> {
                    cachedJoke = null
                    FailedJokeUiModel(noCachedJokes.getMessage())
                }
            }
        } else {
            return when (val result = cloudDataSource.getJoke()) {
                is Result.Success<JokeServerModel> -> {
                    result.data.toJoke().let {
                        cachedJoke = it
                        it.toBaseJoke()
                    }
                }
                is Result.Error<ErrorType> -> {
                    cachedJoke = null
                    val failure = if (result.exception == ErrorType.NO_CONNECTION) noConnection
                    else serviceUnavailable
                    FailedJokeUiModel(failure.getMessage())
                }
            }
        }
    }

    override suspend fun changeJokeStatus(): JokeUiModel? = cachedJoke?.change(cacheDataSource)

    override fun chooseDataSource(isFavorite: Boolean) {
        getJokeFromCache = isFavorite
    }

    override fun clear() {
        jokeCallback = null
    }
}

class TestModel(resourceManager: ResourceManager) : Model {
    private var jokeCallback: JokeCallback? = null
    private var count = 1
    private val serviceUnavailable by lazy { ServiceUnavailable(resourceManager) }

    override fun init(callback: JokeCallback) {
        jokeCallback = callback
    }

    override suspend fun getJoke(): JokeUiModel {
        withContext(Dispatchers.IO) {
            Thread.sleep(1000)
        }
        if (count < 2) count++ else count = 0
        return when (count) {
            0 -> BaseJokeUiModel("baseSetup", "basePunchline")
            1 -> FavoriteJokeUiModel("favoriteSetup", "favoritePunchline")
            2 -> FailedJokeUiModel(serviceUnavailable.getMessage())
            else -> FailedJokeUiModel("Unreachable msg")
        }

    }

    override suspend fun changeJokeStatus(): JokeUiModel? {
        TODO("Not yet implemented")
    }

    override fun chooseDataSource(isFavorite: Boolean) {
        TODO("Not yet implemented")
    }

    override fun clear() {
        jokeCallback = null
    }
}

interface JokeCallback {
    fun provide(jokeUiModel: JokeUiModel)
}