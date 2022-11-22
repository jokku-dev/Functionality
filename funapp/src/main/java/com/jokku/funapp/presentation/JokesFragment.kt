package com.jokku.funapp.presentation

import com.jokku.funapp.FunApp
import com.jokku.funapp.R

class JokesFragment : BaseFragment<Int>() {
    override fun getViewModel(app: FunApp) = app.jokeViewModel
    override fun getCommunicator(app: FunApp) = app.jokeCommunicator
    override fun checkBoxText() = R.string.show_favorite_joke
    override fun actionButtonText() = R.string.get_joke
}