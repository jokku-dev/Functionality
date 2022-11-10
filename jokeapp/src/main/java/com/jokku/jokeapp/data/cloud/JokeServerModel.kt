package com.jokku.jokeapp.data.cloud

import com.google.gson.annotations.SerializedName
import com.jokku.jokeapp.core.Mapper
import com.jokku.jokeapp.data.RepoModel

data class JokeServerModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("punchline")
    val punchline: String,
    @SerializedName("setup")
    val setup: String,
    @SerializedName("type")
    val type: String
) : Mapper<RepoModel> {

    override fun map() = RepoModel(id, setup, punchline)
}