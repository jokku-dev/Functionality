package com.jokku.funapp.data.cache

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class JokeRealmModel : RealmObject {
    @PrimaryKey
    var id: Int = -1
    var setup: String = ""
    var punchline: String = ""
}

open class QuoteRealmModel : RealmObject {
    @PrimaryKey
    var id: String = ""
    var content: String = ""
    var author: String = ""
}