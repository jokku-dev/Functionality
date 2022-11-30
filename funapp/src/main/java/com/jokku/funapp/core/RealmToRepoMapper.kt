package com.jokku.funapp.core

import com.jokku.funapp.data.RepoModel
import com.jokku.funapp.data.cache.JokeRealmModel
import com.jokku.funapp.data.cache.QuoteRealmModel
import io.realm.kotlin.types.RealmObject

interface RealmToRepoMapper<T : RealmObject, E> {
    fun map(realmObject: T): RepoModel<E>
}

class JokeRealmToRepoMapper : RealmToRepoMapper<JokeRealmModel, Int> {
    override fun map(realmObject: JokeRealmModel) =
        RepoModel(realmObject.id, realmObject.setup, realmObject.punchline, true)
}

class QuoteRealmToRepoMapper : RealmToRepoMapper<QuoteRealmModel, String> {
    override fun map(realmObject: QuoteRealmModel) =
        RepoModel(realmObject.id, realmObject.author, realmObject.content, true)
}