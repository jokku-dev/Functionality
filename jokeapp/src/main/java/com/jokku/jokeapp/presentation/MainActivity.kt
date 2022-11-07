package com.jokku.jokeapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jokku.jokeapp.JokeApplication
import com.jokku.jokeapp.R

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val favoriteDataView = findViewById<FavoriteDataView>(R.id.jokeDataView)

        mainViewModel = (application as JokeApplication).mainViewModel
        mainViewModel.observe(this) { state ->
            favoriteDataView.show(state)
        }

        favoriteDataView.handleGetButton {
            mainViewModel.getJoke()
        }
        favoriteDataView.listenFavoriteCheckBox { isChecked ->
            mainViewModel.chooseFavorites(isChecked)
        }
        favoriteDataView.handleFavoriteButton {
            mainViewModel.changeJokeStatus()
        }

        if (savedInstanceState == null) mainViewModel.getJoke()
    }
}