package com.jokku.funapp.data.cache

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

interface RealmProvider {
    fun provide(): Realm
}

class JokeRealmProvider : RealmProvider {
    override fun provide(): Realm =
        Realm.open(RealmConfiguration.create(setOf(JokeRealmModel::class)))
}

class QuoteRealmProvider : RealmProvider {
    override fun provide(): Realm =
        Realm.open(RealmConfiguration.create(setOf(QuoteRealmModel::class)))
}