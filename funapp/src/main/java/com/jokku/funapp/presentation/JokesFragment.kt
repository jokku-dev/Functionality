package com.jokku.funapp.presentation

import com.jokku.funapp.R

class JokesFragment : BaseFragment<JokesViewModel, Int>() {
    override fun getViewModelClass() = JokesViewModel::class.java
    override fun checkBoxText() = R.string.show_favorite_joke
    override fun actionButtonText() = R.string.get_joke
}