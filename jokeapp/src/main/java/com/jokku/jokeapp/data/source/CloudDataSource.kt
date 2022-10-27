package com.jokku.jokeapp.data.source

import com.jokku.jokeapp.data.entity.JokeServerModel
import com.jokku.jokeapp.model.Joke
import java.net.UnknownHostException

interface CloudDataSource {
    suspend fun getJoke(): Result<JokeServerModel, ErrorType>
}

class BaseCloudDataSource(private val service: JokeService) : CloudDataSource {
    override suspend fun getJoke() : Result<JokeServerModel, ErrorType> {
        return try {
            val result = service.getJoke()
            Result.Success(result)
        } catch (e: Exception) {
            val errorType = if (e is UnknownHostException)
                ErrorType.NO_CONNECTION
            else
                ErrorType.SERVICE_UNAVAILABLE
            Result.Error(errorType)
        }
        /*service.getJoke().enqueue(object : Callback<JokeServerModel> {
            override fun onResponse(
                call: Call<JokeServerModel>,
                response: Response<JokeServerModel>
            ) {
                if (response.isSuccessful) {
                    jokeCloudCallback.provide(response.body()!!.toJoke())
                } else {
                    jokeCloudCallback.fail(ErrorType.SERVICE_UNAVAILABLE)
                }
            }

            override fun onFailure(call: Call<JokeServerModel>, t: Throwable) {
                val errorType = if (t is UnknownHostException)
                    ErrorType.NO_CONNECTION
                else
                    ErrorType.SERVICE_UNAVAILABLE
                jokeCloudCallback.fail(errorType)
            }
        })*/
    }
}

class TestCloudDataSource : CloudDataSource {
    private var count = 0
    override suspend fun getJoke(): Result<JokeServerModel, ErrorType> {
        val joke = JokeServerModel(
            count,"TestPunchline$count","TestSetup$count","TestType"
        )
        count++
        return Result.Success(joke)
    }
}

interface JokeCloudCallback {
    fun provide(joke: Joke)
    fun fail(error: ErrorType)
}

enum class ErrorType {
    NO_CONNECTION,
    SERVICE_UNAVAILABLE
}