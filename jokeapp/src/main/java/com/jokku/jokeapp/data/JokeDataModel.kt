package com.jokku.jokeapp.data

import com.jokku.jokeapp.core.JokeMapper
import com.jokku.jokeapp.data.cache.JokeChanger
import com.jokku.jokeapp.data.cache.JokeStatusChanger

class JokeDataModel(
    private val id: Int,
    private val setup: String,
    private val punchline: String,
    private val cached: Boolean = false
) : JokeChanger {

    override suspend fun changeModelType(jokeStatusChanger: JokeStatusChanger): JokeDataModel {
        return jokeStatusChanger.addOrRemove(id, this)
    }
    fun <T> map(mapper: JokeMapper<T>): T {
        return mapper.map(id, setup, punchline, cached)
    }

    fun changeCached(isCached: Boolean): JokeDataModel {
        return JokeDataModel(id, setup, punchline, isCached)
    }
}