package com.jokku.funapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.jokku.funapp.FunApp
import com.jokku.funapp.R
import com.jokku.funapp.presentation.fragment.BaseFragment
import com.jokku.funapp.presentation.fragment.JokesFragment
import com.jokku.funapp.presentation.fragment.QuotesFragment

class MainActivity : AppCompatActivity() {

    private val screens = listOf(JokesFragment::class.java, QuotesFragment::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProvider(
            this, (application as FunApp).viewModelFactory
        )[MainViewModel::class.java]
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val tabChosen: (Int) -> Unit = { position ->
            viewModel.save(position)
        }
        tabLayout.addOnTabSelectedListener(TabListener(tabChosen))

        viewModel.observe(this) { position ->
            tabLayout.getTabAt(position)?.select()
            show(screens[position].newInstance())
        }
        viewModel.init()
    }

    private fun show(fragment: BaseFragment<*, *>) = with(supportFragmentManager) {
        if (fragments.isEmpty() || fragments.last().tag != fragment.tag)
            beginTransaction().replace(R.id.container, fragment, fragment.tag()).commit()
    }
}

private class TabListener(private val tabChosen: (Int) -> Unit) :
    TabLayout.OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab?) = tabChosen.invoke(tab?.position ?: 0)
    override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
    override fun onTabReselected(tab: TabLayout.Tab?) = Unit
}