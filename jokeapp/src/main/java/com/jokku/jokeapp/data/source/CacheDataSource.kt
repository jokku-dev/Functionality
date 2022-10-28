package com.jokku.jokeapp.data.source

import com.jokku.jokeapp.data.entity.Joke
import com.jokku.jokeapp.data.entity.JokeRealmModel
import com.jokku.jokeapp.model.JokeUiModel
import io.realm.kotlin.ext.query

interface CacheDataSource {
    suspend fun addOrRemove(id: Int, joke: Joke): JokeUiModel
    suspend fun getJoke(): Result<Joke, Unit>
}

class BaseCacheDataSource(private val realmProvider: RealmProvider) : CacheDataSource {
    override suspend fun addOrRemove(id: Int, joke: Joke): JokeUiModel = realmProvider.provide().writeBlocking {
        val jokeRealmModel = this.query<JokeRealmModel>("id == $id", id).first().find()
        if (jokeRealmModel == null) {
            val newJoke = joke.toRealmJoke()
            this.copyToRealm(newJoke)
            joke.toFavoriteJoke()
        } else {
            delete(jokeRealmModel)
            joke.toBaseJoke()
        }
    }

    override suspend fun getJoke(): Result<Joke, Unit> = realmProvider.provide().writeBlocking {
        val jokes = this.query<JokeRealmModel>().find()
        if (jokes.isEmpty())
            Result.Error(Unit)
        else
            jokes.random().let { joke ->
                Result.Success(
                    Joke(
                        joke.id,
                        joke.punchline,
                        joke.setup,
                        joke.type
                    )
                )
            }
    }
}