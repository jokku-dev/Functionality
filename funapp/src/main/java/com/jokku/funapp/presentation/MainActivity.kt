package com.jokku.funapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.jokku.funapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val tabChosen: (Boolean) -> Unit = { jokesChosen ->
            if (jokesChosen) show(JokesFragment()) else show(QuotesFragment())
        }
        tabLayout.addOnTabSelectedListener(TabListener(tabChosen))
        show(JokesFragment())
    }

    private fun show(fragment: BaseFragment<*, *>) = with(supportFragmentManager) {
        if (fragments.isEmpty() || fragments.last().tag != fragment.tag)
            beginTransaction().replace(R.id.container, fragment, fragment.tag()).commit()
    }
}

private class TabListener(private val tabChosen: (Boolean) -> Unit) :
    TabLayout.OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab?) = tabChosen.invoke(tab?.position == 0)
    override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
    override fun onTabReselected(tab: TabLayout.Tab?) = Unit
}