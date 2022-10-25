package com.jokku.jokeapp.model

import com.jokku.jokeapp.R
import com.jokku.jokeapp.util.ResourceManager

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
