package com.jokku.funapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jokku.funapp.FunApplication
import com.jokku.funapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val jokeViewModel = (application as FunApplication).mainViewModel
        val jokeFunDataView = findViewById<FunDataView>(R.id.jokeDataView)
        jokeViewModel.observe(this) { state ->
            jokeFunDataView.show(state)
        }

        val quoteViewModel = (application as FunApplication).quoteViewModel
        val quoteFunDataView = findViewById<FunDataView>(R.id.quoteDataView)
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

        quoteFunDataView.handleGetButton {
            quoteViewModel.getItem()
        }
        quoteFunDataView.listenFavoriteCheckBox { isChecked ->
            quoteViewModel.chooseFavorites(isChecked)
        }
        quoteFunDataView.handleFavoriteButton {
            quoteViewModel.changeItemStatus()
        }
    }
}