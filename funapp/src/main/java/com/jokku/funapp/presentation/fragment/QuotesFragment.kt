package com.jokku.funapp.presentation.fragment

import com.jokku.funapp.R

class QuotesFragment : BaseFragment<QuotesViewModel, String>() {
    override fun getViewModelClass() = QuotesViewModel::class.java
    override fun checkBoxText() = R.string.show_favorite_quote
    override fun actionButtonText() = R.string.get_quote
}