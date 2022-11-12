package com.jokku.funapp.domain

import com.jokku.funapp.core.Mapper
import com.jokku.funapp.presentation.*

sealed class DomainItem : Mapper<UiModel> {

    class Success(
        private val firstText: String,
        private val secondText: String,
        private val favorite: Boolean
    ) : DomainItem() {

        override fun map(): UiModel {
            return if (favorite) FavoriteUiModel(firstText, secondText)
            else BaseUiModel(firstText, secondText)
        }
    }

    class Failed(
        private val failureMessenger: FailureMessenger
    ) : DomainItem() {

        override fun map(): UiModel {
            return FailedUiModel(failureMessenger.getMessage())
        }
    }
}















