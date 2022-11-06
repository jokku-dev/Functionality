package com.jokku.jokeapp.data.source

import com.jokku.jokeapp.data.entity.JokeServerModel
import com.jokku.jokeapp.data.entity.NewJokeServerModel
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