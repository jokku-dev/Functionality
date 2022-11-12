package com.jokku.funapp.data.cloud

import com.google.gson.annotations.SerializedName
import com.jokku.funapp.core.Mapper
import com.jokku.funapp.data.RepoModel

class JokeServerModel(
    @SerializedName("id")
    private val id: Int,
    @SerializedName("punchline")
    private val punchline: String,
    @SerializedName("setup")
    private val setup: String
) : Mapper<RepoModel<Int>> {

    override fun map() = RepoModel(id, setup, punchline)
}