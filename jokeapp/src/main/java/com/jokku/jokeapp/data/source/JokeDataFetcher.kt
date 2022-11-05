package com.jokku.jokeapp.data.source

import com.jokku.jokeapp.data.entity.JokeDataModel

interface JokeDataFetcher {
    suspend fun getJoke(): JokeDataModel
}