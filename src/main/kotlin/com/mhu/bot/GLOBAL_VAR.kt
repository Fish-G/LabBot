package com.mhu.bot

object GLOBALVAR {
    const val VEXU_VERIFIED_ROLE_ID:String = "1309208306522394704"
    const val VEXU_GUILD_ID:String = "985845363556626463"
    const val VEXU_ROLES_CHANNEL:String = "1157147777768362085"
    const val VEXU_INTRODUCTIONS_CHANNEL:String = "1389106594637156393"
    const val TEST_GUILD_ID:String = "763467841013284945"
    const val VEXU_GENERAL_ID:String = "1152082077584478278"


    fun welcomeMessage(userHandle:String):String {
        return "Welcome $userHandle, you have been verified. You can now select your roles in <#$VEXU_ROLES_CHANNEL> and can post an introduction to <#$VEXU_INTRODUCTIONS_CHANNEL>."
    }
}
