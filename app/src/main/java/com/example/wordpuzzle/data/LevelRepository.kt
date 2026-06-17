package com.example.wordpuzzle.data

import android.content.Context
import com.example.wordpuzzle.data.model.Level
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LevelRepository(private val context: Context) {

    private var cachedLevels: List<Level>? = null

    fun getLevels(): List<Level> {
        if (cachedLevels != null) return cachedLevels!!

        val json = context.assets.open("levels.json")
            .bufferedReader()
            .use { it.readText() }

        val type = object : TypeToken<List<Level>>() {}.type
        cachedLevels = Gson().fromJson(json, type)
        return cachedLevels!!
    }

    fun getLevel(id: Int): Level? {
        return getLevels().find { it.id == id }
    }

    fun getNextLevel(currentId: Int): Level? {
        return getLevels().find { it.id == currentId + 1 }
    }
}

