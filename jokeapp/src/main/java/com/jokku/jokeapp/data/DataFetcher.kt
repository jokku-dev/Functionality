package com.jokku.jokeapp.data

interface DataFetcher {
    suspend fun getData(): RepoModel
}