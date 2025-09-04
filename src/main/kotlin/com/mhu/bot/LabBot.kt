package com.mhu.bot

import com.fazecast.jSerialComm.SerialPort
import commands.CommandManager
import io.github.cdimascio.dotenv.Dotenv
import kotlinx.coroutines.*
import kotlinx.coroutines.time.delay
import listeners.EventListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import java.lang.Exception
import java.time.Duration

class SerialPortNotFoundException : Exception("SerialPort Not Found")

class LabBot {
    private val config: Dotenv = Dotenv.configure().load()
    private val leaderboard = Database().also { it.loadLeaderboard() }
    fun run() = runBlocking {

        val api: JDA = JDABuilder.createDefault(config.get("TOKEN"))
            .enableIntents(GatewayIntent.GUILD_MEMBERS)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .build()

        api.addEventListener(CommandManager(leaderboard, api), EventListener(leaderboard, api))

        //api.presence.activity = Activity.playing("VEX U HIGH STAKES")
        api.awaitReady()
        //Load Member Caches
        api.getGuildById(GLOBALVAR.VEXU_GUILD_ID)!!.loadMembers()

        println("ports: ")
        try {
            SerialPort.getCommPorts().forEach { println(it) }

            val comPort = SerialPort.getCommPorts()[0]
            comPort.openPort()
            val doorStatus = launch { doorStatus(api, comPort) }
        } catch (e: SerialPortNotFoundException) {
            println("Door Status will not launch")
        }


        val leaderboardSaver = launch { autosaveLeaderboard(Duration.ofDays(1)) }

        println("bot ready")
    }

    private suspend fun doorStatus(api: JDA, port: SerialPort) = coroutineScope {
        var state = false

        while (true) {
            if (port.bytesAvailable() > 0) {
                var buffer: ByteArray = ByteArray(3)
                port.readBytes(buffer, 3)
                val output = String(buffer, Charsets.US_ASCII)[0]
                println(output)
                if (output == '1' && !state) { // 0 closed
                    api.presence.activity = Activity.customStatus("DOOR OPEN")
                    state = true
                } else if (output == '0' && state) {
                    api.presence.activity = Activity.customStatus("DOOR CLOSED")
                    state = false
                }
            }
        }
    }

    private suspend fun autosaveLeaderboard(duration: Duration) {
        while (true) {
            leaderboard.saveLeaderboard()
            println("leaderboard saved")
            delay(duration)
        }
    }
}

fun main() {
    LabBot().run()
    println()
}