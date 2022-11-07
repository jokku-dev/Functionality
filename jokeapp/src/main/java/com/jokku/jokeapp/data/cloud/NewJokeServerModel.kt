package com.jokku.jokeapp.data.cloud

import com.google.gson.annotations.SerializedName
import com.jokku.jokeapp.core.Mapper
import com.jokku.jokeapp.data.JokeDataModel

class NewJokeServerModel(
    @SerializedName("id")
    private val id: Int,
    @SerializedName("setup")
    private val setup: String,
    @SerializedName("delivery")
    private val punchline: String
) : Mapper<JokeDataModel> {

    override fun map() = JokeDataModel(id, setup, punchline)
}