package com.sharehands.sharehands_frontend.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.sharehands.sharehands_frontend.model.schedule.CheckListItem

class SharedPreferencesManager private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("data", Context.MODE_PRIVATE)

    companion object {
        @Volatile private var instance: SharedPreferencesManager? = null

        fun getInstance(context: Context): SharedPreferencesManager {
            return instance ?: synchronized(this) {
                instance ?: SharedPreferencesManager(context).also { instance = it }
            }
        }
    }

    // SharedPreferences를 다루는 함수들 작성
    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun saveInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue) ?: defaultValue
    }

    fun deleteIntByKey(key: String) {
        sharedPreferences.edit()
            .remove(key)
            .apply()
    }

    fun deleteStringByKey(key: String) {
        sharedPreferences.edit()
            .remove(key)
            .apply()
    }

    fun deleteAllKeys() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun getArray(
        key: String,
        defaultValue: MutableList<CheckListItem> = mutableListOf()
    ): MutableList<CheckListItem> {
        val str = sharedPreferences.getString(key, "[]") ?: "[]"
        return Gson().fromJson(str, Array<CheckListItem>::class.java).toMutableList()
    }

    fun addArray(key: String, newItem: CheckListItem) {
        val itemsJson = sharedPreferences.getString(key, "[]") ?: "[]"
        val items = Gson().fromJson(itemsJson, Array<CheckListItem>::class.java).toMutableList()
        items.add(newItem)
        val updatedItemsJson = Gson().toJson(items)
        sharedPreferences.edit()
            .putString(key, updatedItemsJson)
            .apply()
    }

    fun deleteArray(key: String, newList: MutableList<CheckListItem>) {
        val items = Gson().toJson(newList)
        sharedPreferences.edit()
            .putString(key, items)
            .apply()
    }

}