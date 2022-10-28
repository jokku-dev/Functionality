package com.jokku.jokeapp.data.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class JokeRealmModel() : RealmObject {
    @PrimaryKey
    var id: Int = -1
    var setup: String = ""
    var punchline: String = ""
    var type: String = ""
}