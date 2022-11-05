package com.jokku.jokeapp.presentation

import android.os.Bundle
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.jokku.jokeapp.JokeApplication
import com.jokku.jokeapp.R
import com.jokku.jokeapp.presentation.model.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = (application as JokeApplication).mainViewModel
        val textView = findViewById<CorrectTextView>(R.id.joke_tv)
        val favoriteBtn = findViewById<CorrectImageButton>(R.id.favorite_ib)
        val getBtn = findViewById<CorrectButton>(R.id.joke_btn)
        val checkBox = findViewById<CheckBox>(R.id.favourite_joke_cb)
        val progressBar = findViewById<CorrectProgressBar>(R.id.progress_bar)

        mainViewModel.observe(this) { state ->
            state.show(progressBar, getBtn, textView, favoriteBtn)
        }

        getBtn.setOnClickListener {
            mainViewModel.getJoke()
        }
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            mainViewModel.chooseFavorites(isChecked)
        }
        favoriteBtn.setOnClickListener {
            mainViewModel.changeJokeStatus()
        }

        if (savedInstanceState == null) getBtn.performClick()
    }
}