package com.jokku.jokeapp.data.entity

import com.jokku.jokeapp.data.JokeChanger
import com.jokku.jokeapp.core.Mapper
import com.jokku.jokeapp.data.source.JokeStatusChanger

class JokeDataModel(
    private val id: Int,
    private val setup: String,
    private val punchline: String,
    private val cached: Boolean = false
) : JokeChanger {

    override suspend fun changeModelType(jokeStatusChanger: JokeStatusChanger): JokeDataModel {
        return jokeStatusChanger.addOrRemove(id, this)
    }
    fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(id, setup, punchline, cached)
    }

    fun changeCached(isCached: Boolean): JokeDataModel {
        return JokeDataModel(id, setup, punchline, isCached)
    }
}