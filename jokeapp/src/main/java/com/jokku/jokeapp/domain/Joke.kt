package com.jokku.jokeapp.domain

import com.jokku.jokeapp.presentation.model.*

sealed class Joke {
    abstract fun map(): JokeUiModel

    class Success(
        private val setup: String,
        private val punchline: String,
        private val favorite: Boolean
    ) : Joke() {

        override fun map(): JokeUiModel {
            return if (favorite) FavoriteJokeUiModel(setup, punchline)
            else BaseJokeUiModel(setup, punchline)
        }
    }

    class Failed(
        private val failure: JokeFailure
    ) : Joke() {

        override fun map(): JokeUiModel {
            return FailedJokeUiModel(failure.getMessage())
        }
    }
}















