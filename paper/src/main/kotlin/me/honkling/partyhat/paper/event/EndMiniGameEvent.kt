package me.honkling.partyhat.paper.event

import me.honkling.partyhat.common.event.EndMiniGameEventRef
import me.honkling.partyhat.paper.minigame.MiniGame
import org.bukkit.event.HandlerList

private val handlerList = HandlerList()

class EndMiniGameEvent(miniGame: MiniGame<*>) : MiniGameEvent(miniGame), EndMiniGameEventRef {
    companion object {
        @JvmStatic
        fun getHandlerList() = handlerList
    }

    override fun getHandlers() = handlerList
}