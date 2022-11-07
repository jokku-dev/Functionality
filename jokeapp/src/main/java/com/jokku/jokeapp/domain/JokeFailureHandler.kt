package com.jokku.jokeapp.domain

import com.jokku.jokeapp.ResourceManager
import com.jokku.jokeapp.presentation.*

interface JokeFailureHandler {
    fun handle(e: Exception): JokeFailure
}

class JokeFailureFactory(private val resourceManager: ResourceManager) : JokeFailureHandler {

    override fun handle(e: Exception): JokeFailure {
        return when (e) {
            is NoConnectionException -> NoConnection(resourceManager)
            is NoCachedJokesException -> NoCachedJokes(resourceManager)
            is ServiceUnavailableException -> ServiceUnavailable(resourceManager)
            else -> GenericError(resourceManager)
        }
    }
}