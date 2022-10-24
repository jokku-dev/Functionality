package com.jokku.jokeapp

import com.google.gson.annotations.SerializedName

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