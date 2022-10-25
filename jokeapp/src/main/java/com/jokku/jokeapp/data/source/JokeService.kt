package com.jokku.jokeapp.data.source

import com.jokku.jokeapp.data.entity.JokeServerModel
import retrofit2.Call
import retrofit2.http.GET

interface JokeService {
    @GET("https://official-joke-api.appspot.com/random_joke/")
    fun getJoke() : Call<JokeServerModel>
}