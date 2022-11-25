package com.jokku.funapp.domain

import com.jokku.funapp.ResourceManager
import com.jokku.funapp.presentation.content.*

interface FailureHandler {
    fun handle(e: Exception): FailureMessenger
}

class FailureFactory(private val resourceManager: ResourceManager) : FailureHandler {
    override fun handle(e: Exception): FailureMessenger {
        return when (e) {
            is NoConnectionException -> NoConnection(resourceManager)
            is NoCachedDataException -> NoCachedData(resourceManager)
            is ServiceUnavailableException -> ServiceUnavailable(resourceManager)
            else -> GenericError(resourceManager)
        }
    }
}