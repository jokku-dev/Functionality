package com.jokku.funapp.data.cloud

import com.google.gson.annotations.SerializedName
import com.jokku.funapp.core.Mapper
import com.jokku.funapp.data.RepoModel

class QuoteServerModel(
    @SerializedName("_id")
    private val id: String,
    @SerializedName("content")
    private val content: String,
    @SerializedName("author")
    private val author: String
) : Mapper<RepoModel<String>> {

    override fun map() = RepoModel(id, content, author)
}