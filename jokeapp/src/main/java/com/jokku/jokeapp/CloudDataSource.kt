package com.jokku.jokeapp

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException

interface CloudDataSource {
    fun getJoke(jokeCloudCallback: JokeCloudCallback)
}

class BaseCloudDataSource(private val service: JokeService) : CloudDataSource {
    override fun getJoke(jokeCloudCallback: JokeCloudCallback) {
        service.getJoke().enqueue(object : Callback<JokeServerModel> {
            override fun onResponse(
                call: Call<JokeServerModel>, response: Response<JokeServerModel>
            ) {
                if (response.isSuccessful) {
                    jokeCloudCallback.provide(response.body()!!)
                } else {
                    jokeCloudCallback.fail(ErrorType.SERVICE_UNAVAILABLE)
                }
            }

            override fun onFailure(call: Call<JokeServerModel>, t: Throwable) {
                if (t is UnknownHostException)
                    jokeCloudCallback.fail(ErrorType.NO_CONNECTION)
                else
                    jokeCloudCallback.fail(ErrorType.SERVICE_UNAVAILABLE)
            }
        })
    }
}

class TestCloudDataSource : CloudDataSource {
    override fun getJoke(jokeCloudCallback: JokeCloudCallback) {
        jokeCloudCallback.provide(JokeServerModel(0,"TestPunchline","TestSetup","TestType"))
    }
}

interface JokeCloudCallback {
    fun provide(joke: JokeServerModel)
    fun fail(error: ErrorType)
}

enum class ErrorType {
    NO_CONNECTION,
    SERVICE_UNAVAILABLE
}