package com.jokku.funapp.core

import com.jokku.funapp.data.cache.JokeRealmModel
import com.jokku.funapp.data.cache.QuoteRealmModel
import com.jokku.funapp.domain.DomainItem

interface FromRepoMapper<T, E> {
    fun map(id: E, firstText: String, secondText: String, cached: Boolean): T
}

class JokeRealmMapper : FromRepoMapper<JokeRealmModel, Int> {
    override fun map(id: Int, firstText: String, secondText: String, cached: Boolean) =
        JokeRealmModel().also { joke ->
            joke.id = id
            joke.setup = firstText
            joke.punchline = secondText
        }
}

class QuoteRealmMapper : FromRepoMapper<QuoteRealmModel, String> {
    override fun map(id: String, firstText: String, secondText: String, cached: Boolean) =
        QuoteRealmModel().also { quote ->
            quote.id = id
            quote.author = firstText
            quote.content = secondText
        }
}

class DomainSuccessMapper<E> : FromRepoMapper<DomainItem.Success, E> {
    override fun map(id: E, firstText: String, secondText: String, cached: Boolean) =
        DomainItem.Success(firstText, secondText, cached)
}