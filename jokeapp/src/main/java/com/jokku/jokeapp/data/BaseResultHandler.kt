package com.jokku.jokeapp.data

import com.jokku.jokeapp.data.entity.Joke
import com.jokku.jokeapp.data.entity.JokeServerModel
import com.jokku.jokeapp.data.source.ErrorType
import com.jokku.jokeapp.data.source.JokeDataFetcher
import com.jokku.jokeapp.model.FailedJokeUiModel
import com.jokku.jokeapp.model.JokeFailure
import com.jokku.jokeapp.model.JokeUiModel

abstract class BaseResultHandler<S, E>(
    private val jokeDataFetcher: JokeDataFetcher<S, E>
) {
    suspend fun process(): JokeUiModel {
        return handleResult(jokeDataFetcher.getJoke())
    }

    protected abstract fun handleResult(result: Result<S, E>): JokeUiModel
}

class CloudResultHandler(
    private val cachedJoke: CachedJoke,
    jokeDataFetcher: JokeDataFetcher<JokeServerModel, ErrorType>,
    private val noConnection: JokeFailure,
    private val serviceUnavailable: JokeFailure
) : BaseResultHandler<JokeServerModel, ErrorType>(jokeDataFetcher) {

    override fun handleResult(result: Result<JokeServerModel, ErrorType>) = when (result) {
        is Result.Success<JokeServerModel> -> {
            result.data.toJoke().let {
                cachedJoke.saveJoke(it)
                it.toBaseUiJoke()
            }
        }
        is Result.Error<ErrorType> -> {
            cachedJoke.clear()
            val failure = if (result.exception == ErrorType.NO_CONNECTION) noConnection
            else serviceUnavailable
            FailedJokeUiModel(failure.getMessage())
        }
    }
}

class CacheResultHandler(
    private val cachedJoke: CachedJoke,
    jokeDataFetcher: JokeDataFetcher<Joke, Unit>,
    private val noCachedJokes: JokeFailure
) : BaseResultHandler<Joke, Unit>(jokeDataFetcher) {

    override fun handleResult(result: Result<Joke, Unit>) = when (result) {
        is Result.Success<Joke> -> result.data.let {
            cachedJoke.saveJoke(it)
            it.toFavoriteUiJoke()
        }
        is Result.Error -> {
            cachedJoke.clear()
            FailedJokeUiModel(noCachedJokes.getMessage())
        }
    }
}