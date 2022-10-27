package com.jokku.jokeapp.data.entity

import com.google.gson.annotations.SerializedName
import com.jokku.jokeapp.model.Joke

data class JokeServerModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("punchline")
    val punchline: String,
    @SerializedName("setup")
    val setup: String,
    @SerializedName("type")
    val type: String
) {
    fun toJoke() = Joke(id, punchline, setup, type)
}