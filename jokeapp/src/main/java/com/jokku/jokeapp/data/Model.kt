package com.jokku.jokeapp.data

import com.jokku.jokeapp.data.entity.JokeServerModel
import com.jokku.jokeapp.data.source.*
import com.jokku.jokeapp.model.*
import com.jokku.jokeapp.util.ResourceManager

interface Model {
    fun init(callback: JokeCallback)
    fun getJoke()
    fun changeJokeStatus(jokeCallback: JokeCallback)
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
    private var cachedJokeServerModel: JokeServerModel? = null
    private var getJokeFromCache = false

    override fun init(callback: JokeCallback) {
        jokeCallback = callback
    }

    override fun getJoke() {
        if (getJokeFromCache) {
            cacheDataSource.getJoke(object : JokeCachedCallback {
                override fun provide(jokeServerModel: JokeServerModel) {
                    cachedJokeServerModel = jokeServerModel
                    jokeCallback?.provide(jokeServerModel.toFavoriteJoke())
                }

                override fun fail() {
                    cachedJokeServerModel = null
                    jokeCallback?.provide(FailedJoke(noCachedJokes.getMessage()))
                }
            })
        } else {
            cloudDataSource.getJoke(object : JokeCloudCallback {
                override fun provide(joke: JokeServerModel) {
                    cachedJokeServerModel = joke
                    jokeCallback?.provide(joke.toBaseJoke())
                }

                override fun fail(error: ErrorType) {
                    cachedJokeServerModel = null
                    val failure =
                        if (error == ErrorType.NO_CONNECTION) noConnection else serviceUnavailable
                    jokeCallback?.provide(FailedJoke(failure.getMessage()))
                }
            })
        }

    }

    override fun changeJokeStatus(jokeCallback: JokeCallback) {
        cachedJokeServerModel?.change(cacheDataSource)?.let {
            jokeCallback.provide(it)
        }
    }

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

    override fun getJoke() {
        Thread.sleep(1000)
        when (count) {
            0 -> jokeCallback?.provide(BaseJoke("baseSetup", "basePunchline"))
            1 -> jokeCallback?.provide(FavoriteJoke("favoriteSetup", "favoritePunchline"))
            2 -> jokeCallback?.provide(FailedJoke(serviceUnavailable.getMessage()))
        }
        if (count < 2) count++ else count = 0
    }

    override fun changeJokeStatus(jokeCallback: JokeCallback) {
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
    fun provide(joke: Joke)
}