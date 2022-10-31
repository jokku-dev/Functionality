package com.jokku.jokeapp.data.source

import com.jokku.jokeapp.data.Result

interface JokeDataFetcher<S, E> {
    suspend fun getJoke(): Result<S, E>
}