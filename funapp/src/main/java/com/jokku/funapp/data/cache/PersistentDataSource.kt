package com.jokku.funapp.data.cache

import android.content.Context
import android.content.SharedPreferences

interface PersistentDataSource {
    fun save(value: Int, name: String, key: String)
    fun load(name: String, key: String): Int

    class Base(private val context: Context) : PersistentDataSource {
        private var sharedPreferences: SharedPreferences? = null

        override fun save(value: Int, name: String, key: String) {
            getPreferences(name).edit().putInt(key, value).apply()
        }

        override fun load(name: String, key: String): Int =
            getPreferences(name).getInt(key, 0)

        private fun getPreferences(name: String): SharedPreferences {
            if (sharedPreferences == null) {
                sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)
            }
            return sharedPreferences!!
        }
    }
}