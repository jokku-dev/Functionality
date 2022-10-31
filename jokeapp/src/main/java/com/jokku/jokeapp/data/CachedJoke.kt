package com.jokku.jokeapp.data

import com.jokku.jokeapp.data.entity.ChangeJoke
import com.jokku.jokeapp.data.entity.Joke
import com.jokku.jokeapp.data.source.ChangeJokeStatus
import com.jokku.jokeapp.model.JokeUiModel

interface CachedJoke : ChangeJoke {
    fun saveJoke(joke: Joke)
    fun clear()
}

class BaseCachedJoke : CachedJoke {
    private var cached: Joke? = null
    override suspend fun change(changeJokeStatus: ChangeJokeStatus): JokeUiModel? {
        return cached?.change(changeJokeStatus)
    }
    override fun saveJoke(joke: Joke) {
        cached = joke
    }
    override fun clear() {
        cached = null
    }
}