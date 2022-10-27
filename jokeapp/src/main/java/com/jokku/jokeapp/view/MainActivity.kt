package com.jokku.jokeapp.view

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.jokku.jokeapp.JokeApplication
import com.jokku.jokeapp.R
import com.jokku.jokeapp.model.DisplayCallback
import com.jokku.jokeapp.model.ViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = (application as JokeApplication).viewModel
        val textView = findViewById<TextView>(R.id.joke_tv)
        val imageBtn = findViewById<ImageButton>(R.id.favorite_ib)
        val button = findViewById<Button>(R.id.joke_btn)
        val checkBox = findViewById<CheckBox>(R.id.favourite_joke_cb)
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)

        progressBar.visibility = ProgressBar.INVISIBLE
        imageBtn.visibility = ImageButton.INVISIBLE

        viewModel.init(object : DisplayCallback {
            override fun provideText(text: String) {
                button.isEnabled = true
                progressBar.visibility = ProgressBar.INVISIBLE
                textView.text = text
            }

            override fun provideIconRes(id: Int) { imageBtn.setImageResource(id) }
        })

        button.setOnClickListener {
            button.isEnabled = false
            progressBar.visibility = ProgressBar.VISIBLE
            viewModel.getJoke()
            imageBtn.visibility = ImageButton.VISIBLE
        }
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.chooseFavorites(isChecked)
        }
        imageBtn.setOnClickListener {
            viewModel.changeJokeStatus()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
    }
}