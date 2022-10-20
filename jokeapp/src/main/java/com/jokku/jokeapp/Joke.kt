package com.jokku.jokeapp

interface UiMapper {
    fun toUiText() : String
}

class Joke(private val text: String, private val punchline: String) : UiMapper {
    override fun toUiText() = "$text\n$punchline"
}