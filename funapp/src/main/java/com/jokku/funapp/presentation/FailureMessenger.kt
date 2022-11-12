package com.jokku.funapp.presentation

import com.jokku.funapp.R
import com.jokku.funapp.ResourceManager

interface FailureMessenger {
    fun getMessage(): String
}
class NoConnection(private val resourceManager: ResourceManager) : FailureMessenger {
    override fun getMessage() = resourceManager.getString(R.string.no_connection)
}

class ServiceUnavailable(private val resourceManager: ResourceManager) : FailureMessenger {
    override fun getMessage() = resourceManager.getString(R.string.service_unavailable)
}

class NoCachedData(private val resourceManager: ResourceManager) : FailureMessenger {
    override fun getMessage() = resourceManager.getString(R.string.no_cached_data)
}

class GenericError(private val resourceManager: ResourceManager) : FailureMessenger {
    override fun getMessage() = resourceManager.getString(R.string.generic_fail_message)
}
