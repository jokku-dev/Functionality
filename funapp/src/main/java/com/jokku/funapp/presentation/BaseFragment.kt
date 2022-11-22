package com.jokku.funapp.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jokku.funapp.FunApp
import com.jokku.funapp.R
import com.jokku.funapp.presentation.adapter.RecyclerAdapter

abstract class BaseFragment<T> : Fragment() {
    protected abstract fun getViewModel(app: FunApp): FunViewModel<T>
    protected abstract fun getCommunicator(app: FunApp): Communicator<T>
    protected abstract fun checkBoxText(): Int
    protected abstract fun actionButtonText(): Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application = requireActivity().application as FunApp
        val viewModel = getViewModel(application)
        val communicator = getCommunicator(application)
        val funDataView = view.findViewById<FunDataView>(R.id.funDataView)
        funDataView.linkWith(viewModel)
        funDataView.checkBoxText(checkBoxText())
        funDataView.actionButtonText(actionButtonText())

        viewModel.observe(this) { state ->
            funDataView.show(state)
        }

        val jokeRecyclerView = view.findViewById<RecyclerView>(R.id.jokeRecyclerView)
        val adapter = RecyclerAdapter(object : RecyclerAdapter.FavoriteItemClickListener<T> {
            override fun changeStatus(id: T) {
                Snackbar.make(
                    funDataView,
                    R.string.remove_from_favorites,
                    Snackbar.LENGTH_SHORT
                )
                    .setAction(R.string.yes) {
                        viewModel.changeItemStatus(id)
                    }.show()
            }
        }, communicator)
        jokeRecyclerView.adapter = adapter

        viewModel.observeList(this) { adapter.update() }
    }
}