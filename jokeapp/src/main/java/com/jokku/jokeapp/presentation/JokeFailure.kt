package com.jokku.jokeapp.presentation

import com.jokku.jokeapp.R
import com.jokku.jokeapp.ResourceManager

interface JokeFailure {
    fun getMessage(): String
}

class NoConnection(private val resourceManager: ResourceManager) : JokeFailure {
    override fun getMessage() = resourceManager.getString(R.string.no_connection)
}

class ServiceUnavailable(private val resourceManager: ResourceManager) : JokeFailure {
    override fun getMessage() = resourceManager.getString(R.string.service_unavailable)
}

class NoCachedJokes(private val resourceManager: ResourceManager) : JokeFailure {
    override fun getMessage() = resourceManager.getString(R.string.no_favorite_joke)
}

class GenericError(private val resourceManager: ResourceManager) : JokeFailure {
    override fun getMessage() = resourceManager.getString(R.string.generic_fail_message)
}
