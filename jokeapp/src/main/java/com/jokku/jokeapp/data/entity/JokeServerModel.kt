package com.jokku.jokeapp.data.entity

import com.google.gson.annotations.SerializedName
import com.jokku.jokeapp.core.Mapper

data class JokeServerModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("punchline")
    val punchline: String,
    @SerializedName("setup")
    val setup: String,
    @SerializedName("type")
    val type: String
) : Mapper<JokeDataModel> {

    override fun map() = JokeDataModel(id, setup, punchline)
}