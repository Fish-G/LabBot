package commands

import com.mhu.bot.Database
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.guild.GuildReadyEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands

class CommandManager(val leaderboard: Database) : ListenerAdapter() {
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {

    }

    override fun onGuildReady(event: GuildReadyEvent) {
        val commandData = mutableListOf<CommandData>()

        commandData.add(Commands.slash("website", "The Rutgers SCAR Website"))
        commandData.add(Commands.slash("fire", "Most fired person"))
        commandData.add(Commands.slash("skull", "Most skulled person"))
        commandData.add(Commands.slash("saveleaderboard", "save leaderboards"))

        event.guild.updateCommands().addCommands(commandData).queue()
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.guild == null) return

        when (event.name) {
            "website" -> displayWebsite(event)
            "fire" -> fireLeaderboard(event)
            "skull" -> skullLeaderboard(event)
            "saveleaderboard" -> {
                leaderboard.saveLeaderboard()
                event.reply("Done").queue()
            }
        }
    }

    fun displayWebsite(event: SlashCommandInteractionEvent) {
        event.reply("vex website is : https://www.google.com/").queue()
    }

    fun fireLeaderboard(event: SlashCommandInteractionEvent) {
        val s = leaderboard.leaderboards.entries.sortedByDescending { (k, v) -> v[0] }.joinToString { (k, v) -> "${event.jda.getUserById(k)!!.effectiveName} : ${v[1]}\n" }
        event.reply(s).queue()
    }

    fun skullLeaderboard(event: SlashCommandInteractionEvent) {
        val s = leaderboard.leaderboards.entries.sortedByDescending { (k, v) -> v[1] }.joinToString { (k, v) -> "${event.jda.getUserById(k)!!.effectiveName} : ${v[1]}\n" }
        event.reply(s).queue()
    }
}