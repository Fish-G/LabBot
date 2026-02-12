package com.mhu.bot

import io.github.cdimascio.dotenv.Dotenv
import io.github.crackthecodeabhi.kreds.connection.Endpoint
import io.github.crackthecodeabhi.kreds.connection.newClient
import kotlinx.coroutines.*

enum class EmoteType { SKULL, FIRE }
class Database {
    val leaderboards: HashMap<String, MutableList<Int>> = HashMap() // {fires, skulls}
    private val config: Dotenv = Dotenv.configure().load()
    private val client = runBlocking {
        newClient(Endpoint.from(config.get("DB_ENDPOINT")))
    }

    private fun authencate() {
        runBlocking {
            client.auth(
                config.get("DB_USERNAME"),
                config.get("DB_PASSWORD")
            )
        }
    }

    fun loadLeaderboard() {
        authencate()
        runBlocking {
            leaderboards.clear()
            val keys = client.keys("*")
            for (k in keys) {
                val s = client.get(k)!!.split(",")
                leaderboards[k] = mutableListOf(s[0].toInt(), s[1].toInt())
            }
        }
        println("Leaderboard Loaded")
    }

    fun saveLeaderboard() {
        try {
            authencate()
            runBlocking {
                leaderboards.entries.forEach { (k, v) ->
                    client.set(k, v.joinToString(","))
                }
            }
            println("Leaderboard Saved")
        } catch (e: Exception) {
            println("Failed to save leaderboard")
        }
    }

    fun leaderboardInc(user: String, type: EmoteType) {
        when (type) {
            EmoteType.FIRE -> if (!leaderboards.containsKey(user)) leaderboards[user] =
                mutableListOf(1, 0) else leaderboards[user]!![0]++

            EmoteType.SKULL -> if (!leaderboards.containsKey(user)) leaderboards[user] =
                mutableListOf(0, 1) else leaderboards[user]!![1]++
        }
    }

    fun leaderboardDec(user: String, type: EmoteType) {
        when (type) {
            EmoteType.FIRE -> if (!leaderboards.containsKey(user)) leaderboards[user] =
                mutableListOf(-1, 0) else leaderboards[user]!![0]--

            EmoteType.SKULL -> if (!leaderboards.containsKey(user)) leaderboards[user] =
                mutableListOf(0, -1) else leaderboards[user]!![1]--
        }
    }
}