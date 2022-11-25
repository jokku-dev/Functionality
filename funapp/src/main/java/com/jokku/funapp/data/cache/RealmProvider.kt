package com.jokku.funapp.data.cache

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class RealmProvider {
    fun provide(): Realm =
        Realm.open(RealmConfiguration.create(setOf(JokeRealmModel::class, QuoteRealmModel::class)))
}
