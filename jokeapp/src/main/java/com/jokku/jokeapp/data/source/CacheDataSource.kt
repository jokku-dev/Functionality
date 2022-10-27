package com.jokku.jokeapp.data.source

import com.jokku.jokeapp.data.entity.JokeRealmModel
import com.jokku.jokeapp.model.Joke
import com.jokku.jokeapp.model.JokeUiModel
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface CacheDataSource {
    suspend fun addOrRemove(id: Int, joke: Joke): JokeUiModel
    suspend fun getJoke(): Result<Joke, Unit>
}

class BaseCacheDataSource(private val realm: Realm) : CacheDataSource {
    override suspend fun addOrRemove(id: Int, joke: Joke): JokeUiModel =
        withContext(Dispatchers.IO) {
            realm.writeBlocking {
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
        }

    override suspend fun getJoke(): Result<Joke, Unit> {
        return realm.writeBlocking {
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
}

class TestCacheDataSource : CacheDataSource {
    private val list = ArrayList<Pair<Int, Joke>>()

    override suspend fun addOrRemove(id: Int, joke: Joke): JokeUiModel {
        val found = list.find { it.first == id }
        return if (found != null) {
            val uiModel = found.second.toBaseJoke()
            list.remove(found)
            uiModel
        } else {
            list.add(Pair(id, joke))
            joke.toFavoriteJoke()
        }
    }

    override suspend fun getJoke(): Result<Joke, Unit> {
        return if (list.isEmpty())
            Result.Error(Unit)
        else
            Result.Success(list.random().second)

    }
}

interface JokeCachedCallback {
    fun provide(joke: Joke)
    fun fail()
}