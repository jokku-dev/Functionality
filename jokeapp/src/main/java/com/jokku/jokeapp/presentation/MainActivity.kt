package com.jokku.jokeapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jokku.jokeapp.FunApplication
import com.jokku.jokeapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val jokeViewModel = (application as FunApplication).mainViewModel
        val jokeFunDataView = findViewById<FunDataView>(R.id.jokeDataView)
        jokeFunDataView.linkWith(jokeViewModel)
        jokeViewModel.observe(this) { state ->
            jokeFunDataView.show(state)
        }

        val quoteViewModel = (application as FunApplication).quoteViewModel
        val quoteFunDataView = findViewById<FunDataView>(R.id.quoteDataView)
        quoteFunDataView.linkWith(quoteViewModel)
        quoteViewModel.observe(this) { state ->
            quoteFunDataView.show(state)
        }

        jokeFunDataView.handleGetButton {
            jokeViewModel.getItem()
        }
        jokeFunDataView.listenFavoriteCheckBox { isChecked ->
            jokeViewModel.chooseFavorites(isChecked)
        }
        jokeFunDataView.handleFavoriteButton {
            jokeViewModel.changeItemStatus()
        }

        if (savedInstanceState == null) jokeViewModel.getItem()
    }
}