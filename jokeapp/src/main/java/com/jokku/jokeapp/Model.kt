package com.jokku.jokeapp

interface Model {
    fun init(callback: JokeCallback)
    fun getJoke()
    fun changeJokeStatus(jokeCallback: JokeCallback)
    fun clear()
}

class BaseModel(
    private val cacheDataSource: CacheDataSource,
    private val cloudDataSource: CloudDataSource,
    private val resourceManager: ResourceManager
) : Model {
    private val noConnection by lazy { NoConnection(resourceManager) }
    private val serviceUnavailable by lazy { ServiceUnavailable(resourceManager) }
    private var jokeCallback: JokeCallback? = null
    private var cachedJokeServerModel: JokeServerModel? = null

    override fun init(callback: JokeCallback) {
        jokeCallback = callback
    }

    override fun getJoke() {
        cloudDataSource.getJoke(object : JokeCloudCallback {
            override fun provide(joke: JokeServerModel) {
                cachedJokeServerModel = joke
                jokeCallback?.provide(joke.toBaseJoke())
            }

            override fun fail(error: ErrorType) {
                cachedJokeServerModel = null
                val failure = if (error == ErrorType.NO_CONNECTION) noConnection else serviceUnavailable
                jokeCallback?.provide(FailedJoke(failure.getMessage()))
            }
        })
    }

    override fun changeJokeStatus(jokeCallback: JokeCallback) {
        cachedJokeServerModel?.change(cacheDataSource)?.let {
            jokeCallback.provide(it)
        }
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

    override fun clear() {
        jokeCallback = null
    }
}

interface JokeCallback {
    fun provide(joke: Joke)
}