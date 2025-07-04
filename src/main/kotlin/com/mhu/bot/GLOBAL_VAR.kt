package com.mhu.bot

object GLOBALVAR {
    const val VERIFIED_ROLE_ID:String = "1309208306522394704"
    const val IEEE_GUILD_ID:String = "985845363556626463"
    const val IEEE_ROLES_CHANNEL:String = "1157147777768362085"
    const val IEEE_INTRODUCTIONS_CHANNEL:String = "1389106594637156393"
    const val TEST_GUILD_ID:String = "763467841013284945"
    const val IEEE_GENERAL_ID:String = "1152082077584478278"


    fun welcomeMessage(userHandle:String):String {
        return "Welcome $userHandle, you have been verified. You can now select your roles in <#$IEEE_ROLES_CHANNEL> and can post an introduction to <#$IEEE_INTRODUCTIONS_CHANNEL>."
    }
}
