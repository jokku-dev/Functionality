package com.jokku.jokeapp.domain

import com.jokku.jokeapp.core.Mapper
import com.jokku.jokeapp.data.JokeRepository

interface JokeInteractor {
    suspend fun getJoke(): Joke
    suspend fun changeFavorites(): Joke
    suspend fun getFavoriteJoke(isFavorite: Boolean)
}

class BaseJokeInteractor(
    private val repository: JokeRepository,
    private val failureHandler: JokeFailureHandler,
    private val mapper: Mapper<Joke.Success>
) : JokeInteractor {

    override suspend fun getJoke(): Joke {
        return try {
            repository.getJoke().map(mapper)
        } catch (e: Exception) {
            Joke.Failed(failureHandler.handle(e))
        }
    }

    override suspend fun changeFavorites(): Joke {
        return try {
            repository.changeJokeStatus().map(mapper)
        } catch (e: Exception) {
            Joke.Failed(failureHandler.handle(e))
        }
    }

    override suspend fun getFavoriteJoke(isFavorite: Boolean) {
        repository.chooseDataSource(isFavorite)
    }
}