package com.jokku.funapp.data.cloud.mock

import com.jokku.funapp.data.cloud.BaseCloudDataSource
import com.jokku.funapp.data.cloud.JokeServerModel
import com.jokku.funapp.data.cloud.QuoteServerModel
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface SimpleCall<T> : Call<T> {
    override fun clone(): Call<T> = throw IllegalStateException("not used")
    override fun enqueue(callback: Callback<T>) = Unit
    override fun isExecuted(): Boolean = false
    override fun cancel() = Unit
    override fun isCanceled(): Boolean = false
    override fun request(): Request = throw IllegalStateException("not used")
    override fun timeout(): Timeout = throw IllegalStateException("not used")
}

class MockJokeCloudDataSource : BaseCloudDataSource<JokeServerModel, Int>() {
    private var id: Int = -1

    override fun getServerModel(): Call<JokeServerModel> {
        return object : SimpleCall<JokeServerModel> {
            override fun execute(): Response<JokeServerModel> {
                ++id
                return Response.success(JokeServerModel(
                    id, "mock punchline $id", "mock setup $id"
                ))
            }
        }
    }
}

class MockQuoteCloudDataSource : BaseCloudDataSource<QuoteServerModel, String>() {
    private var id: String = "-1"

    override fun getServerModel(): Call<QuoteServerModel> {
        return object : SimpleCall<QuoteServerModel> {
            override fun execute(): Response<QuoteServerModel> {
                id = id.toInt().plus(1).toString()
                return Response.success(QuoteServerModel(
                    id, "mock content $id", "mock author $id"
                ))
            }
        }
    }
}