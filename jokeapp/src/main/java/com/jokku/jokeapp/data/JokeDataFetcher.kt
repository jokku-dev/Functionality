package com.jokku.jokeapp.data

interface JokeDataFetcher {
    suspend fun getJoke(): JokeDataModel
}