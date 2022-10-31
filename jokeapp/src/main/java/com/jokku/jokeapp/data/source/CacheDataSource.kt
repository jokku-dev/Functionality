package com.jokku.jokeapp.data.source

import com.jokku.jokeapp.data.Result
import com.jokku.jokeapp.data.entity.Joke
import com.jokku.jokeapp.data.entity.JokeRealmModel
import com.jokku.jokeapp.model.JokeUiModel
import com.jokku.jokeapp.util.RealmProvider
import io.realm.kotlin.ext.query

interface CacheDataSource : JokeDataFetcher<Joke, Unit>, ChangeJokeStatus

interface ChangeJokeStatus {
    suspend fun addOrRemove(id: Int, joke: Joke): JokeUiModel
}

class BaseCacheDataSource(private val realmProvider: RealmProvider) : CacheDataSource {
    override suspend fun addOrRemove(id: Int, joke: Joke): JokeUiModel = realmProvider.provide().writeBlocking {
        val jokeRealmModel = this.query<JokeRealmModel>("id == $id", id).first().find()
        if (jokeRealmModel == null) {
            val newJoke = joke.toRealmJoke()
            this.copyToRealm(newJoke)
            joke.toFavoriteUiJoke()
        } else {
            delete(jokeRealmModel)
            joke.toBaseUiJoke()
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