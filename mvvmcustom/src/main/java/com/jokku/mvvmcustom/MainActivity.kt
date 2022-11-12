package com.jokku.mvvmcustom

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jokku.mvvmcustom.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ViewModel
    private lateinit var counterView: TextView
    private lateinit var startBtn: Button
    private lateinit var pauseBtn: Button
    private lateinit var resetBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = (application as MyApplication).viewModel
        counterView = binding.textCounter
        startBtn = binding.startBtn
        pauseBtn = binding.pauseBtn
        resetBtn = binding.resetBtn

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