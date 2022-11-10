package com.jokku.jokeapp.data.cloud

import com.google.gson.annotations.SerializedName
import com.jokku.jokeapp.core.Mapper
import com.jokku.jokeapp.data.RepoModel

class QuoteServerModel(
    @SerializedName("id")
    private val id: Int,
    @SerializedName("content")
    private val content: String,
    @SerializedName("author")
    private val author: String
    ) : Mapper<RepoModel> {

    override fun map() = RepoModel(id, content, author)
}