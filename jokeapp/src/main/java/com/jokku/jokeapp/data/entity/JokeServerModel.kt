package com.jokku.jokeapp.data.entity

import com.google.gson.annotations.SerializedName
import com.jokku.jokeapp.data.source.CacheDataSource
import com.jokku.jokeapp.model.BaseJoke
import com.jokku.jokeapp.model.FavoriteJoke

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
    fun toBaseJoke() = BaseJoke(setup, punchline)

    fun toFavoriteJoke() = FavoriteJoke(setup, punchline)

    fun change(cacheDataSource: CacheDataSource) = cacheDataSource.addOrRemove(id,this)
}