package com.jokku.funapp.presentation

import com.jokku.funapp.FunApp
import com.jokku.funapp.R

class QuotesFragment : BaseFragment<String>() {
    override fun getViewModel(app: FunApp) = app.quoteViewModel
    override fun getCommunicator(app: FunApp) = app.quoteCommunicator
    override fun checkBoxText() = R.string.show_favorite_quote
    override fun actionButtonText() = R.string.get_quote
}