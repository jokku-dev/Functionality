package com.jokku.funapp.domain

import com.jokku.funapp.core.Mapper
import com.jokku.funapp.presentation.*

sealed class DomainItem<E> : Mapper<UiModel<E>> {

    class Success<E>(
        private val id: E,
        private val firstText: String,
        private val secondText: String,
        private val favorite: Boolean
    ) : DomainItem<E>() {

        override fun map(): UiModel<E> {
            return if (favorite) FavoriteUiModel(id, firstText, secondText)
            else BaseUiModel(firstText, secondText)
        }
    }

    class Failed<E>(
        private val failureMessenger: FailureMessenger
    ) : DomainItem<E>() {

        override fun map(): UiModel<E> {
            return FailedUiModel(failureMessenger.getMessage())
        }
    }
}















