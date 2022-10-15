package com.jokku.mvvmcustom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.jokku.mvvm_custom.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ViewModel
    private lateinit var counterView: TextView
    private lateinit var startBtn: Button
    private lateinit var pauseBtn: Button
    private lateinit var resetBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = (application as MyApplication).viewModel
        counterView = findViewById(R.id.text_counter)
        startBtn = findViewById(R.id.start_btn)
        pauseBtn = findViewById(R.id.pause_btn)
        resetBtn = findViewById(R.id.reset_btn)

        val textObservable = TextObservable()
        textObservable.observe(object : TextCallback{
            override fun updateText(str: String) = runOnUiThread {
                counterView.text = str
            }
        })
        viewModel.init(textObservable)
        startBtn.isEnabled = false

        initBtns(textObservable)
    }

    override fun onResume() {
        super.onResume()
        viewModel.startCounting()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopCounting()

    }

    private fun initBtns(observable: TextObservable) {
        startBtn.setOnClickListener {
            viewModel.init(observable)
            viewModel.startCounting()
            startBtn.isEnabled = false
            pauseBtn.isEnabled = true
        }
        pauseBtn.setOnClickListener {
            viewModel.stopCounting()
            pauseBtn.isEnabled = false
            startBtn.isEnabled = true
        }
        resetBtn.setOnClickListener {
            viewModel.resetCounting()
            startBtn.isEnabled = true
            pauseBtn.isEnabled = false
        }
    }
}