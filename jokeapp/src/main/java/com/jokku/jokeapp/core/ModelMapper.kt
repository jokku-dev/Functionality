package com.jokku.jokeapp.core

import com.jokku.jokeapp.data.RepoModel
import com.jokku.jokeapp.data.cache.JokeRealmModel
import com.jokku.jokeapp.data.cache.QuoteRealmModel
import com.jokku.jokeapp.domain.DomainItem

interface ModelMapper<T> {
    fun map(id: Int, firstText: String, secondText: String, cached: Boolean): T

}

class SuccessMapper : ModelMapper<DomainItem.Success> {
    override fun map(id: Int, firstText: String, secondText: String, cached: Boolean) =
        DomainItem.Success(firstText, secondText, cached)
}

class JokeRealmMapper : ModelMapper<JokeRealmModel> {
    override fun map(id: Int, firstText: String, secondText: String, cached: Boolean) =
        JokeRealmModel().also { joke ->
            joke.id = id
            joke.setup = firstText
            joke.punchline = secondText
        }
}

class QuoteRealmMapper : ModelMapper<QuoteRealmModel> {
    override fun map(id: Int, firstText: String, secondText: String, cached: Boolean) =
        QuoteRealmModel().also { quote ->
            quote.id = id
            quote.author = firstText
            quote.content = secondText
        }
}

class DataModelMapper : ModelMapper<RepoModel> {
    override fun map(id: Int, firstText: String, secondText: String, cached: Boolean) =
        RepoModel(id, firstText, secondText, cached)
}