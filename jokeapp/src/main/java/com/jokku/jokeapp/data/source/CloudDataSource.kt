package com.jokku.jokeapp.data.source

import com.jokku.jokeapp.data.entity.JokeDataModel
import com.jokku.jokeapp.domain.NoConnectionException
import com.jokku.jokeapp.domain.ServiceUnavailableException
import java.net.UnknownHostException

interface CloudDataSource : JokeDataFetcher

class BaseCloudDataSource(private val service: JokeService) : CloudDataSource {
    override suspend fun getJoke() : JokeDataModel {
        try {
            return service.getJoke().execute().body()!!.map()
        } catch (e: Exception) {
            if (e is UnknownHostException) { //low-level exception
                throw NoConnectionException() //high level exceptions
            } else {
                throw ServiceUnavailableException()
            }
        }
    }
}