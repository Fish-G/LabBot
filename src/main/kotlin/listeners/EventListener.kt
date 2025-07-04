package listeners

import com.mhu.bot.Database
import com.mhu.bot.EmoteType
import com.mhu.bot.GLOBALVAR
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class EventListener(val leaderboard: Database, val api: JDA) : ListenerAdapter() {
    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        if (event.emoji.name == "\uD83D\uDC80") {
            println("skulled")
            leaderboard.leaderboardInc(event.messageAuthorId,EmoteType.SKULL)
        }
        if (event.emoji.name == "\uD83D\uDD25") {
            println("fired")
            leaderboard.leaderboardInc(event.messageAuthorId,EmoteType.FIRE)
        }
    }

    override fun onMessageReactionRemove(event: MessageReactionRemoveEvent) {
        if (event.emoji.name == "\uD83D\uDC80") {
            println("skulled")
            println()
            leaderboard.leaderboardDec(event.channel.retrieveMessageById(event.messageId).complete().author.id,EmoteType.SKULL)

        }
        if (event.emoji.name == "\uD83D\uDD25") {
            println("fired")
            leaderboard.leaderboardDec(event.channel.retrieveMessageById(event.messageId).complete().author.id,EmoteType.FIRE)
        }
    }

    override fun onGuildMemberRoleAdd(event: GuildMemberRoleAddEvent) {
        println("onGuildMemeberRoleAdd event trigger")
        if (GLOBALVAR.VEXU_VERIFIED_ROLE_ID in event.roles.map {it.id}.toList()) {
            event.member.user.openPrivateChannel().flatMap { channel -> channel.sendMessage(GLOBALVAR.welcomeMessage(event.member.asMention)) }.queue()
            event.guild.getTextChannelById(GLOBALVAR.VEXU_GENERAL_ID)!!.sendMessage(GLOBALVAR.welcomeMessage(event.member.asMention)).queue()
        }
    }

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        when (event.componentId) {
            "approve" -> approveMemberVerification(event)
            "deny" -> denyMemberVerification(event)
        }
    }

    private fun approveMemberVerification(event: ButtonInteractionEvent) {
        event.message.editMessageEmbeds().setComponents().queue()

        val user = event.message.mentions.users[0]
        event.guild!!.addRoleToMember(user, api.getRoleById(GLOBALVAR.VEXU_VERIFIED_ROLE_ID)!!).queue()
        event.reply("${event.user.asMention} has approved ${user.asMention}").queue()
    }

    private fun denyMemberVerification(event: ButtonInteractionEvent) {
        event.message.editMessageEmbeds().setComponents().queue()
        val user = event.message.mentions.users[0]
        user.openPrivateChannel().flatMap { channel -> channel.sendMessage(GLOBALVAR.verificationRejectedMessage(user.asMention)) }.queue()
        event.reply("${event.user.asMention} has denied ${user.asMention}").queue()
    }
}