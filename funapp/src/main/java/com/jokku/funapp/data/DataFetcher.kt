package com.jokku.funapp.data

interface DataFetcher<E> {
    suspend fun getData(): RepoModel<E>
}