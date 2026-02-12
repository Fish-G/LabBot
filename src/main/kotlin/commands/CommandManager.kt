package commands

import com.mhu.bot.Database
import com.mhu.bot.GLOBALVAR
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.guild.GuildReadyEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.components.buttons.Button

class CommandManager(val leaderboard: Database, val api:JDA) : ListenerAdapter() {
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {

    }

    override fun onGuildReady(event: GuildReadyEvent) {
        val commandData = mutableListOf<CommandData>()

        commandData.add(Commands.slash("website", "The Rutgers SCAR Website"))
        commandData.add(Commands.slash("fire", "Most fired person"))
        commandData.add(Commands.slash("skull", "Most skulled person"))
        commandData.add(Commands.slash("saveleaderboard", "save leaderboards"))
        commandData.add(Commands.slash("verify", "Submit a verification request for Rutgers VEX U")
            .addOption(OptionType.STRING,"name","Please write your name",true)
            .addOption(OptionType.STRING, "netid", "Your Rutgers netid",true)
        )
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
            "verify" -> verification(event)
        }
    }

    fun displayWebsite(event: SlashCommandInteractionEvent) {
        event.reply("vex website is : https://www.scarrobotics.com/").queue()
    }

    fun fireLeaderboard(event: SlashCommandInteractionEvent) {
        val s = leaderboard.leaderboards.entries.sortedByDescending { (k, v) -> v[0] }.joinToString("") { (k, v) -> if (v[0] != 0) "${api.retrieveUserById(k).complete().effectiveName} : ${v[0]}\n" else ""}
        event.reply(s).queue()
    }

    fun skullLeaderboard(event: SlashCommandInteractionEvent) {
        val s = leaderboard.leaderboards.entries.sortedByDescending { (k, v) -> v[1] }.joinToString("") { (k, v) -> if (v[1] != 0) "${api.retrieveUserById(k).complete().effectiveName} : ${v[1]}\n" else ""}
        event.reply(s).queue()
    }

    fun verification(event: SlashCommandInteractionEvent) {
        event.reply(GLOBALVAR.verificationPendingMessage(event.user.asMention)).setEphemeral(true).queue()
        event.user.openPrivateChannel().flatMap { channel -> channel.sendMessage(GLOBALVAR.verificationPendingMessage(event.user.asMention)) }.queue()
        event.guild!!.getTextChannelById(GLOBALVAR.VEXU_VERIFY_APPROVE_CHANNEL)!!
            .sendMessage("${event.getOption("name")!!.asString}, netid ${event.getOption("netid")!!.asString} with handle ${event.user.asMention} requests verification")
            .addActionRow(
                Button.success("approve",Emoji.fromUnicode("U+2705")),
                Button.danger("deny", Emoji.fromUnicode("U+274E"))
            ).queue()
    }
}