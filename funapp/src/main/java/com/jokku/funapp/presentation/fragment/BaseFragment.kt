package com.jokku.funapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jokku.funapp.FunApp
import com.jokku.funapp.R
import com.jokku.funapp.presentation.adapter.RecyclerAdapter
import com.jokku.funapp.presentation.content.FunDataView

abstract class BaseFragment<V : BaseViewModel<T>, T> : Fragment() {
    protected abstract fun getViewModelClass(): Class<V>
    @StringRes
    protected abstract fun checkBoxText(): Int
    @StringRes
    protected abstract fun actionButtonText(): Int

    private lateinit var viewModel: BaseViewModel<T>

    fun tag(): String = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            (requireActivity().application as FunApp).viewModelFactory
        )[getViewModelClass()]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        }, viewModel.communicator)
        jokeRecyclerView.adapter = adapter
        viewModel.observeList(this) { adapter.update() }

        viewModel.getItemList()
    }
}