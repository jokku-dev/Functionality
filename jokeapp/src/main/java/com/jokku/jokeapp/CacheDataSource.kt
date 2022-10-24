package com.jokku.jokeapp

interface CacheDataSource {
    fun addOrRemove(id: Int, jokeServerModel: JokeServerModel): Joke
}

class TestCacheDataSource : CacheDataSource {
    private val map = HashMap<Int, JokeServerModel>()

    override fun addOrRemove(id: Int, jokeServerModel: JokeServerModel): Joke {
        return if (map.containsKey(id)) {
            val joke = map[id]!!.toBaseJoke()
            map.remove(id)
            joke
        } else {
            map[id] = jokeServerModel
            jokeServerModel.toFavoriteJoke()
        }
    }
}