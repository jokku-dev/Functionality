package com.jokku.jokeapp.data.source

import com.jokku.jokeapp.data.RealmProvider
import com.jokku.jokeapp.data.entity.JokeDataModel
import com.jokku.jokeapp.data.entity.JokeRealmModel
import com.jokku.jokeapp.domain.NoCachedJokesException
import io.realm.kotlin.ext.query

interface CacheDataSource : JokeDataFetcher, JokeStatusChanger

interface JokeStatusChanger {
    suspend fun addOrRemove(id: Int, joke: JokeDataModel): JokeDataModel
}

class BaseCacheDataSource(private val realmProvider: RealmProvider) : CacheDataSource {
    override suspend fun addOrRemove(id: Int, joke: JokeDataModel): JokeDataModel =
        realmProvider.provide().writeBlocking {
            val jokeRealmModel = this.query<JokeRealmModel>("id == $id", id).first().find()
            if (jokeRealmModel == null) {
                val newJoke = joke.toRealm()
                this.copyToRealm(newJoke)
                joke.changeCached(true)
            } else {
                delete(jokeRealmModel)
                joke.changeCached(false)
            }
        }

    override suspend fun getJoke(): JokeDataModel = realmProvider.provide().writeBlocking {
        val jokes = this.query<JokeRealmModel>().find()
        if (jokes.isEmpty()) throw NoCachedJokesException()
        else jokes.random().map()
    }
}