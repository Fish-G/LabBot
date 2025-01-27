package listeners

import com.mhu.bot.Database
import com.mhu.bot.EmoteType
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEmojiEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class EventListener(val leaderboard: Database) : ListenerAdapter() {
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
}