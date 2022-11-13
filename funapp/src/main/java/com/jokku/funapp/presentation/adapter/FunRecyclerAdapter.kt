package com.jokku.funapp.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jokku.funapp.R
import com.jokku.funapp.data.RepoModel
import com.jokku.funapp.presentation.CorrectTextView

class FunRecyclerAdapter<E> : RecyclerView.Adapter<FunRecyclerAdapter<E>.FunViewHolder<E>>() {
    private val list = ArrayList<RepoModel<E>>()

    @SuppressLint("NotifyDataSetChanged")
    fun show(data: List<RepoModel<E>>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunViewHolder<E> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fun_item, parent, false)
        return FunViewHolder(view)
    }

    override fun onBindViewHolder(holder: FunViewHolder<E>, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class FunViewHolder<E>(view: View) : RecyclerView.ViewHolder(view) {
        private val textView = itemView.findViewById<CorrectTextView>(R.id.fun_textView)

        fun bind(model: RepoModel<E>) {
            model.setText(textView)
        }
    }
}