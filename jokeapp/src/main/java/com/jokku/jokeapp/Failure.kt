package com.jokku.jokeapp

interface Failure {
    fun getMessage(): String
}

class NoConnection(private val resourceManager: ResourceManager) : Failure {
    override fun getMessage() = resourceManager.getString(R.string.no_connection)
}

class ServiceUnavailable(private val resourceManager: ResourceManager) : Failure {
    override fun getMessage() = resourceManager.getString(R.string.service_unavailable)
}