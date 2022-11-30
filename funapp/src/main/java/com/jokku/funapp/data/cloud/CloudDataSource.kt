package com.jokku.funapp.data.cloud

import com.jokku.funapp.core.Mapper
import com.jokku.funapp.data.DataFetcher
import com.jokku.funapp.data.RepoModel
import com.jokku.funapp.domain.NoConnectionException
import com.jokku.funapp.domain.ServiceUnavailableException
import retrofit2.Call
import java.net.UnknownHostException

interface CloudDataSource<E> : DataFetcher<E>

abstract class BaseCloudDataSource<T : Mapper<RepoModel<E>>, E> : CloudDataSource<E> {

    protected abstract fun getServerModel(): Call<T>

    override suspend fun getData(): RepoModel<E> {
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
) : BaseCloudDataSource<JokeServerModel, Int>() {

    override fun getServerModel(): Call<JokeServerModel> = service.getJoke()
}

class NewJokeCloudDataSource(
    private val service: NewJokeService
) : BaseCloudDataSource<NewJokeServerModel, Int>() {

    override fun getServerModel(): Call<NewJokeServerModel> = service.getJoke()
}

class QuoteCloudDataSource(
    private val service: QuoteService
) : BaseCloudDataSource<QuoteServerModel, String>() {

    override fun getServerModel(): Call<QuoteServerModel> = service.getQuote()
}