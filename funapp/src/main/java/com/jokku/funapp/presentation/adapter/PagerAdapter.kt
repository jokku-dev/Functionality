package com.jokku.funapp.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jokku.funapp.presentation.JokesFragment
import com.jokku.funapp.presentation.QuotesFragment

class PagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 2
    override fun createFragment(position: Int) : Fragment =
        if (position == 0) JokesFragment() else QuotesFragment()
}