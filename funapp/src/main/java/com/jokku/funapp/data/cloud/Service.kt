package com.jokku.funapp.data.cloud

import retrofit2.Call
import retrofit2.http.GET

interface BaseJokeService {
    @GET("https://official-joke-api.appspot.com/random_joke/")
    fun getJoke() : Call<JokeServerModel>
}

interface NewJokeService {
    @GET("https://v2.jokeapi.dev/joke/Any")
    fun getJoke() : Call<NewJokeServerModel>
}

interface QuoteService {
    @GET("https://api.quotable.io/random")
    fun getQuote() : Call<QuoteServerModel>
}