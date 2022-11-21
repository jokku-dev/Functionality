package com.jokku.funapp.presentation

import androidx.recyclerview.widget.DiffUtil

class DiffUtilCallback<E>(
    private val oldList: List<UiModel<E>>,
    private val newList: List<UiModel<E>>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].same(newList[newItemPosition])
    }
    //we don't need this due to sufficiency of the item comparison for this case
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = false
}