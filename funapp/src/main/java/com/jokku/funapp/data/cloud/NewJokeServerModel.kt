package com.jokku.funapp.data.cloud

import com.google.gson.annotations.SerializedName
import com.jokku.funapp.core.Mapper
import com.jokku.funapp.data.RepoModel

class NewJokeServerModel(
    @SerializedName("id")
    private val id: Int,
    @SerializedName("setup")
    private val setup: String,
    @SerializedName("delivery")
    private val punchline: String
) : Mapper<RepoModel<Int>> {

    override fun map() = RepoModel(id, setup, punchline)
}