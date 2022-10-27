package com.jokku.jokeapp.model

import com.jokku.jokeapp.data.entity.JokeRealmModel
import com.jokku.jokeapp.data.source.CacheDataSource

class Joke(
    private val id: Int,
    private val punchline: String,
    private val setup: String,
    private val type: String
) {
    fun toBaseJoke() = BaseJokeUiModel(setup, punchline)

    fun toFavoriteJoke() = FavoriteJokeUiModel(setup, punchline)

    fun toRealmJoke(): JokeRealmModel {
        return JokeRealmModel().also {
            it.id = id
            it.setup = setup
            it.punchline = punchline
            it.type = type
        }
    }

    suspend fun change(cacheDataSource: CacheDataSource) = cacheDataSource.addOrRemove(id,this)
}