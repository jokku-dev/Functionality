package com.jokku.jokeapp.data.entity

import com.jokku.jokeapp.data.JokeChanger
import com.jokku.jokeapp.data.source.JokeStatusChanger

class JokeDataModel(
    private val id: Int,
    val setup: String,
    val punchline: String,
    val cached: Boolean = false
) : JokeChanger {

    override suspend fun changeModelType(jokeStatusChanger: JokeStatusChanger): JokeDataModel {
        return jokeStatusChanger.addOrRemove(id, this)
    }

    fun toRealm() = JokeRealmModel().also { joke ->
        joke.id = id
        joke.setup = setup
        joke.punchline = punchline
    }

    fun changeCached(isCached: Boolean): JokeDataModel {
        return JokeDataModel(id, setup, punchline, isCached)
    }
}