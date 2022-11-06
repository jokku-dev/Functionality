package com.jokku.jokeapp.data.entity

import com.jokku.jokeapp.core.Mapper
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class JokeRealmModel : RealmObject {
    @PrimaryKey
    var id: Int = -1
    var setup: String = ""
    var punchline: String = ""

    fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(id, setup, punchline, true)
    }
}