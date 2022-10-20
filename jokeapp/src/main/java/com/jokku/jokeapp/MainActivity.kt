package com.jokku.jokeapp

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = (application as JokeApplication).viewModel

        val textView = findViewById<TextView>(R.id.joke_tv)
        val button = findViewById<Button>(R.id.joke_btn)
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        progressBar.visibility = ProgressBar.INVISIBLE

        button.setOnClickListener {
            button.isEnabled = false
            progressBar.visibility = ProgressBar.VISIBLE
            viewModel.getJoke()
        }

        viewModel.init(object : TextCallback {
            override fun provideText(text: String) = runOnUiThread {
                button.isEnabled = true
                progressBar.visibility = ProgressBar.INVISIBLE
                textView.text = text
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
    }
}