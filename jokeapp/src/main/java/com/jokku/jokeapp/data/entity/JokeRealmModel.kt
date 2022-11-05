package com.jokku.jokeapp.data.entity

import com.jokku.jokeapp.core.Mapper
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class JokeRealmModel : RealmObject, Mapper<JokeDataModel> {
    @PrimaryKey
    var id: Int = -1
    var setup: String = ""
    var punchline: String = ""

    override fun map() = JokeDataModel(id, setup, punchline,true)
}