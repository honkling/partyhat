package me.honkling.partyhat.paper.event

import me.honkling.partyhat.common.event.StartMiniGameEventRef
import me.honkling.partyhat.paper.minigame.MiniGame
import org.bukkit.event.HandlerList

private val handlerList = HandlerList()

class StartMiniGameEvent(miniGame: MiniGame<*>) : MiniGameEvent(miniGame), StartMiniGameEventRef {
    companion object {
        @JvmStatic
        fun getHandlerList() = handlerList
    }

    override fun getHandlers() = handlerList
}