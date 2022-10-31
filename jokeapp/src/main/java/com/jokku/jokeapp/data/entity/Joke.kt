package com.jokku.jokeapp.data.entity

import com.jokku.jokeapp.data.source.ChangeJokeStatus
import com.jokku.jokeapp.model.BaseJokeUiModel
import com.jokku.jokeapp.model.FavoriteJokeUiModel
import com.jokku.jokeapp.model.JokeUiModel

interface ChangeJoke {
    suspend fun change(changeJokeStatus: ChangeJokeStatus): JokeUiModel?
}

class Joke(
    private val id: Int,
    private val punchline: String,
    private val setup: String,
    private val type: String
) : ChangeJoke {
    override suspend fun change(changeJokeStatus: ChangeJokeStatus) =
        changeJokeStatus.addOrRemove(id,this)

    fun toBaseUiJoke() = BaseJokeUiModel(setup, punchline)

    fun toFavoriteUiJoke() = FavoriteJokeUiModel(setup, punchline)

    fun toRealmJoke(): JokeRealmModel {
        return JokeRealmModel().also {
            it.id = id
            it.setup = setup
            it.punchline = punchline
            it.type = type
        }
    }
}















