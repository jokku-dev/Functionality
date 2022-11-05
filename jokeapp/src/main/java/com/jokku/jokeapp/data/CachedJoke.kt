package com.jokku.jokeapp.data

import com.jokku.jokeapp.data.entity.JokeDataModel
import com.jokku.jokeapp.data.source.JokeStatusChanger

interface CachedJoke : JokeChanger {
    fun saveJoke(jokeDataModel: JokeDataModel)
    fun clear()
}

interface JokeChanger {
    suspend fun changeModelType(jokeStatusChanger: JokeStatusChanger): JokeDataModel

    class Empty : JokeChanger {
        override suspend fun changeModelType(jokeStatusChanger: JokeStatusChanger): JokeDataModel {
            throw IllegalStateException("empty joke called")
        }
    }
}

class BaseCachedJoke : CachedJoke {
    private var cached: JokeChanger = JokeChanger.Empty()

    override fun saveJoke(jokeDataModel: JokeDataModel) {
        cached = jokeDataModel
    }

    override fun clear() {
        cached = JokeChanger.Empty()
    }

    override suspend fun changeModelType(jokeStatusChanger: JokeStatusChanger): JokeDataModel {
        return cached.changeModelType(jokeStatusChanger)
    }
}