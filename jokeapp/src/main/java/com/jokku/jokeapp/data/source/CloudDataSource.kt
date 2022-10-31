package com.jokku.jokeapp.data.source

import com.jokku.jokeapp.data.Result
import com.jokku.jokeapp.data.entity.JokeServerModel
import java.net.UnknownHostException

interface CloudDataSource : JokeDataFetcher<JokeServerModel, ErrorType>

class BaseCloudDataSource(private val service: JokeService) : CloudDataSource {
    override suspend fun getJoke() : Result<JokeServerModel, ErrorType> {
        return try {
            val result: JokeServerModel = service.getJoke().execute().body()!!
            Result.Success(result)
        } catch (e: Exception) {
            val errorType = if (e is UnknownHostException)
                ErrorType.NO_CONNECTION
            else
                ErrorType.SERVICE_UNAVAILABLE
            Result.Error(errorType)
        }
    }
}

enum class ErrorType {
    NO_CONNECTION,
    SERVICE_UNAVAILABLE
}