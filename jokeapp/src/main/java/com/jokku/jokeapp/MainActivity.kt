package com.jokku.jokeapp

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

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

        viewModel.init(object : DataCallback {
            override fun provideText(text: String) = runOnUiThread {
                button.isEnabled = true
                progressBar.visibility = ProgressBar.INVISIBLE
                textView.text = text
            }

            override fun provideIconRes(id: Int) = runOnUiThread { imageBtn.setImageResource(id) }
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