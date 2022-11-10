package com.jokku.jokeapp.data.cloud

import com.jokku.jokeapp.core.Mapper
import com.jokku.jokeapp.data.DataFetcher
import com.jokku.jokeapp.data.RepoModel
import com.jokku.jokeapp.domain.NoConnectionException
import com.jokku.jokeapp.domain.ServiceUnavailableException
import retrofit2.Call
import java.net.UnknownHostException

interface CloudDataSource : DataFetcher

abstract class BaseCloudDataSource<T : Mapper<RepoModel>> : CloudDataSource {

    protected abstract fun getServerModel(): Call<T>

    override suspend fun getData(): RepoModel {
        try {
            return getServerModel().execute().body()!!.map()
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

    override fun getServerModel(): Call<JokeServerModel> = service.getJoke()
}

class NewJokeCloudDataSource(
    private val service: NewJokeService
) : BaseCloudDataSource<NewJokeServerModel>() {

    override fun getServerModel(): Call<NewJokeServerModel> = service.getJoke()
}

class QuoteCloudDataSource(
    private val service: QuoteService
) : BaseCloudDataSource<QuoteServerModel>() {

    override fun getServerModel(): Call<QuoteServerModel> = service.getQuote()
}