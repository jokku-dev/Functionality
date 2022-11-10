package com.jokku.jokeapp.data.cache

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

interface RealmProvider {
    fun provide(): Realm
}

class BaseRealmProvider : RealmProvider {
    override fun provide(): Realm = Realm.open(RealmConfiguration.create(setOf(RealmModel::class)))
}