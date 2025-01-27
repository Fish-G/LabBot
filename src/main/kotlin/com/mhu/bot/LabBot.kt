package com.mhu.bot

import commands.CommandManager
import io.github.cdimascio.dotenv.Dotenv
import kotlinx.coroutines.*
import kotlinx.coroutines.time.delay
import listeners.EventListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import java.time.Duration

class LabBot {
    private val config: Dotenv = Dotenv.configure().load()
    private val leaderboard = Database()
    fun run() = runBlocking {

        val api: JDA = JDABuilder.createDefault(config.get("TOKEN")).build()
        api.addEventListener(CommandManager(leaderboard), EventListener(leaderboard))
        //api.presence.activity = Activity.playing("VEX U HIGH STAKES")

        //launch door status detector
        val doorStatus = launch {doorStatus()}
        val leaderboardSaver = launch {autosaveLeaderboard(Duration.ofDays(1))}

        println("bot ready")

    }

    private suspend fun doorStatus() = coroutineScope{

    }

    private suspend fun autosaveLeaderboard(duration: Duration) {
        while (true) {
            leaderboard.saveLeaderboard()
            println("leaderboard saved")
            delay(duration)

        }
    }

}

fun main(){
    LabBot().run()
    println()
}