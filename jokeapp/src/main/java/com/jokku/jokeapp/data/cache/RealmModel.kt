package com.jokku.jokeapp.data.cache

import com.jokku.jokeapp.core.Mapper
import com.jokku.jokeapp.data.RepoModel
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

abstract class RealmModel : RealmObject, Mapper<RepoModel>

open class JokeRealmModel : RealmModel() {
    @PrimaryKey
    var id: Int = -1
    var setup: String = ""
    var punchline: String = ""

    override fun map() = RepoModel(id, setup, punchline, true)
}

open class QuoteRealmModel : RealmModel() {
    @PrimaryKey
    var id: Int = -1
    var content: String = ""
    var author: String = ""

    override fun map() = RepoModel(id, content, author, true)

}