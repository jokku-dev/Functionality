package com.jokku.funapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jokku.funapp.FunApplication
import com.jokku.funapp.R
import com.jokku.funapp.presentation.adapter.FunRecyclerAdapter

class FunActivity : AppCompatActivity() {
    private lateinit var adapter: FunRecyclerAdapter<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val jokeViewModel = (application as FunApplication).jokeViewModel
        val jokeCommunicator = (application as FunApplication).jokeCommunicator
        val jokeDataView = findViewById<FunDataView>(R.id.jokeDataView)
        jokeDataView.linkWith(jokeViewModel)
        jokeViewModel.observe(this) { state ->
            jokeDataView.show(state)
        }

        val jokeRecyclerView = findViewById<RecyclerView>(R.id.jokeRecyclerView)
        adapter = FunRecyclerAdapter(object : FunRecyclerAdapter.FavoriteItemClickListener<Int> {
            override fun changeStatus(id: Int) {
                Snackbar.make(
                    jokeDataView,
                    R.string.remove_from_favorites,
                    Snackbar.LENGTH_SHORT
                )
                    .setAction(R.string.yes) {
                        jokeViewModel.changeItemStatus(id)
                    }.show()
            }
        }, jokeCommunicator)

        jokeRecyclerView.adapter = adapter
        jokeViewModel.observeList(this) { adapter.update() }
    }
}