package com.mhu.bot

import java.io.File
import java.io.FileReader
import java.io.FileWriter

enum class EmoteType { SKULL, FIRE }
class Database {
    val leaderboards: HashMap<String, MutableList<Int>> = HashMap() // {fires, skulls}

    init {
        loadLeaderboard()
    }

    fun loadLeaderboard() {
        val f = File("src/main/resources/leaderboard.txt").forEachLine {
            val s = it.split(",")
            leaderboards[s[0]] = mutableListOf(s[1].toInt(), s[2].toInt())
        }
    }

    fun saveLeaderboard() {
        File("src/main/resources/leaderboard.txt").printWriter().use {
            leaderboards.entries.forEach{ (k,v)->it.println("$k,${v[0]},${v[1]}")}
        }
    }

    fun leaderboardInc(user: String, type: EmoteType) {
        when (type) {
            EmoteType.FIRE -> if (!leaderboards.containsKey(user)) leaderboards[user] = mutableListOf(1,0) else leaderboards[user]!![0]++
            EmoteType.SKULL -> if (!leaderboards.containsKey(user)) leaderboards[user] = mutableListOf(0,1) else leaderboards[user]!![1]++
        }
    }

    fun leaderboardDec(user: String, type: EmoteType) {
        when (type) {
            EmoteType.FIRE -> if (!leaderboards.containsKey(user)) leaderboards[user] = mutableListOf(-1,0) else leaderboards[user]!![0]--
            EmoteType.SKULL -> if (!leaderboards.containsKey(user)) leaderboards[user] = mutableListOf(0,-1) else leaderboards[user]!![1]--
        }
    }
}