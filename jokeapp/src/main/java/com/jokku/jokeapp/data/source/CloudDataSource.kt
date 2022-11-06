package com.jokku.jokeapp.data.source

import com.jokku.jokeapp.core.Mapper
import com.jokku.jokeapp.data.entity.JokeDataModel
import com.jokku.jokeapp.data.entity.JokeServerModel
import com.jokku.jokeapp.data.entity.NewJokeServerModel
import com.jokku.jokeapp.domain.NoConnectionException
import com.jokku.jokeapp.domain.ServiceUnavailableException
import retrofit2.Call
import java.net.UnknownHostException

interface CloudDataSource : JokeDataFetcher

abstract class BaseCloudDataSource<T : Mapper<JokeDataModel>> : CloudDataSource {

    protected abstract fun getJokeServerModel(): Call<T>

    override suspend fun getJoke() : JokeDataModel {
        try {
            return getJokeServerModel().execute().body()!!.map()
        } catch (e: Exception) {
            if (e is UnknownHostException) { //low-level exception
                throw NoConnectionException() //high level exceptions
            } else {
                throw ServiceUnavailableException()
            }
        }
    }
}

class JokeCloudDataSource(
    private val service: BaseJokeService
) : BaseCloudDataSource<JokeServerModel>() {
    override fun getJokeServerModel(): Call<JokeServerModel> = service.getJoke()
 }

class NewJokeCloudDataSource(
    private val service: NewJokeService
) : BaseCloudDataSource<NewJokeServerModel>() {
    override fun getJokeServerModel(): Call<NewJokeServerModel> = service.getJoke()
}