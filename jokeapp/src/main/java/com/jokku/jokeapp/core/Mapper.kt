package com.jokku.jokeapp.core

import com.jokku.jokeapp.data.entity.JokeDataModel
import com.jokku.jokeapp.data.entity.JokeRealmModel
import com.jokku.jokeapp.domain.Joke

interface Mapper<T> {
    fun map(id: Int, setup: String, punchline: String, cached: Boolean): T
}

class SuccessMapper : Mapper<Joke.Success> {
    override fun map(id: Int, setup: String, punchline: String, cached: Boolean) =
        Joke.Success(setup, punchline, cached)
}

class RealmMapper : Mapper<JokeRealmModel> {
    override fun map(id: Int, setup: String, punchline: String, cached: Boolean) =
        JokeRealmModel().also { joke ->
            joke.id = id
            joke.setup = setup
            joke.punchline = punchline
        }
}

class DataModelMapper : Mapper<JokeDataModel> {
    override fun map(id: Int, setup: String, punchline: String, cached: Boolean) =
        JokeDataModel(id, setup, punchline, cached)
}