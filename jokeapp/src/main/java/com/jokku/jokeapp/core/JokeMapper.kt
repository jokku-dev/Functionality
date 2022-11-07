package com.jokku.jokeapp.core

import com.jokku.jokeapp.data.JokeDataModel
import com.jokku.jokeapp.data.cache.JokeRealmModel
import com.jokku.jokeapp.domain.Joke

interface JokeMapper<T> {
    fun map(id: Int, setup: String, punchline: String, cached: Boolean): T

}

class SuccessMapper : JokeMapper<Joke.Success> {
    override fun map(id: Int, setup: String, punchline: String, cached: Boolean) =
        Joke.Success(setup, punchline, cached)
}

class RealmMapper : JokeMapper<JokeRealmModel> {
    override fun map(id: Int, setup: String, punchline: String, cached: Boolean) =
        JokeRealmModel().also { joke ->
            joke.id = id
            joke.setup = setup
            joke.punchline = punchline
        }
}

class DataModelMapper : JokeMapper<JokeDataModel> {
    override fun map(id: Int, setup: String, punchline: String, cached: Boolean) =
        JokeDataModel(id, setup, punchline, cached)
}