package com.jokku.jokeapp.view

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.jokku.jokeapp.JokeApplication
import com.jokku.jokeapp.R
import com.jokku.jokeapp.model.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = (application as JokeApplication).mainViewModel
        val textView = findViewById<TextView>(R.id.joke_tv)
        val imageBtn = findViewById<ImageButton>(R.id.favorite_ib)
        val button = findViewById<Button>(R.id.joke_btn)
        val checkBox = findViewById<CheckBox>(R.id.favourite_joke_cb)
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)

        progressBar.visibility = ProgressBar.INVISIBLE
        imageBtn.visibility = ImageButton.INVISIBLE

        mainViewModel.observe(this) { (text, drawableResId) ->
            progressBar.visibility = ProgressBar.INVISIBLE
            textView.text = text
            imageBtn.visibility = ImageButton.VISIBLE
            button.isEnabled = true
            imageBtn.setImageResource(drawableResId)
        }

        button.setOnClickListener {
            button.isEnabled = false
            progressBar.visibility = ProgressBar.VISIBLE
            mainViewModel.getJoke()
        }
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            mainViewModel.chooseFavorites(isChecked)
        }
        imageBtn.setOnClickListener {
            mainViewModel.changeJokeStatus()
        }

        if (savedInstanceState == null) button.performClick()
    }
}