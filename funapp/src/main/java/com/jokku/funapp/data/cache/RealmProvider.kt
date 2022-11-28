package com.jokku.funapp.data.cache

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

interface RealmProvider {
    fun provideRealm(): Realm
}

class BaseRealmProvider(private val useMocks: Boolean) : RealmProvider {
    override fun provideRealm(): Realm {
        val fileName = if (useMocks) MOCKS else NAME
        val config =
            RealmConfiguration.Builder(setOf(JokeRealmModel::class, QuoteRealmModel::class))
                .name(fileName)
                .build()
        return Realm.open(config)
    }

    companion object {
        const val NAME = "funRealm"
        const val MOCKS = "funRealmMocks"
    }
}
