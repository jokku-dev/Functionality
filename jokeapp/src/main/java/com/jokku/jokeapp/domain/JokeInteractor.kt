package com.jokku.jokeapp.domain

import com.jokku.jokeapp.data.JokeRepository

interface JokeInteractor {
    suspend fun getJoke(): Joke
    suspend fun changeFavorites(): Joke
    suspend fun getFavoriteJoke(isFavorite: Boolean)
}

class BaseJokeInteractor(
    private val repository: JokeRepository,
    private val jokeFailureHandler: JokeFailureHandler
) : JokeInteractor {

    override suspend fun getJoke(): Joke {
        return try {
            Joke.Success(repository.getJoke().setup, repository.getJoke().punchline, false)
        } catch (e: Exception) {
            Joke.Failed(jokeFailureHandler.handle(e))
        }
    }

    override suspend fun changeFavorites(): Joke {
        return try {
            val joke = repository.changeJokeStatus()
            Joke.Success(joke.setup, joke.punchline, joke.cached)
        } catch (e: Exception) {
            Joke.Failed(jokeFailureHandler.handle(e))
        }
    }

    override suspend fun getFavoriteJoke(isFavorite: Boolean) {
        repository.chooseDataSource(isFavorite)
    }
}