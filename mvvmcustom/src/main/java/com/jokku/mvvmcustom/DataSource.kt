package com.jokku.mvvmcustom

import android.content.Context
import android.content.Context.MODE_PRIVATE

interface DataSource {

    fun saveInt(key: String, value: Int)

    fun getInt(key: String) : Int
}

class CacheDataSource(context: Context) : DataSource {

    private val sharedPrefs = context.getSharedPreferences("counter", MODE_PRIVATE)

    override fun saveInt(key: String, value: Int) {
        sharedPrefs.edit().putInt(key, value).apply()
    }

    override fun getInt(key: String) : Int {
        return sharedPrefs.getInt(key,0)
    }
}