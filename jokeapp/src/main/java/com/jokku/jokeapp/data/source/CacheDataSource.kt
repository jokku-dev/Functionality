package com.jokku.jokeapp.data.source

import com.jokku.jokeapp.data.entity.JokeRealm
import com.jokku.jokeapp.data.entity.JokeServerModel
import com.jokku.jokeapp.model.Joke
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query

interface CacheDataSource {
    fun addOrRemove(id: Int, jokeServerModel: JokeServerModel): Joke
    fun getJoke(jokeCachedCallback: JokeCachedCallback)
}

class BaseCacheDataSource(private val realm: Realm) : CacheDataSource {
    override fun addOrRemove(id: Int, jokeServerModel: JokeServerModel): Joke {
        return realm.writeBlocking {
            val jokeRealm = this.query<JokeRealm>("id == $id", id).first().find()
            if (jokeRealm == null) {
                val newJoke = jokeServerModel.toJokeRealm()
                this.copyToRealm(newJoke)
                jokeServerModel.toFavoriteJoke()
            } else {
                delete(jokeRealm)
                jokeServerModel.toBaseJoke()
            }
        }
    }

    override fun getJoke(jokeCachedCallback: JokeCachedCallback) {
        realm.writeBlocking {
            val jokes = this.query<JokeRealm>().find()
            if (jokes.isEmpty())
                jokeCachedCallback.fail()
            else
                jokes.random().let { joke ->
                    jokeCachedCallback.provide(
                        JokeServerModel(
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
    private val list = ArrayList<Pair<Int, JokeServerModel>>()

    override fun addOrRemove(id: Int, jokeServerModel: JokeServerModel): Joke {
        val found = list.find { it.first == id }
        return if (found != null) {
            val joke = found.second.toBaseJoke()
            list.remove(found)
            joke
        } else {
            list.add(Pair(id, jokeServerModel))
            jokeServerModel.toFavoriteJoke()
        }
    }

    override fun getJoke(jokeCachedCallback: JokeCachedCallback) {
        if (list.isEmpty())
            jokeCachedCallback.fail()
        else
            jokeCachedCallback.provide(list.random().second)

    }
}

interface JokeCachedCallback {
    fun provide(jokeServerModel: JokeServerModel)
    fun fail()
}