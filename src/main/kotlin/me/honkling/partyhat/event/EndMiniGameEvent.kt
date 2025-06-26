package me.honkling.partyhat.event

import me.honkling.partyhat.minigame.MiniGame
import org.bukkit.event.HandlerList

private val handlerList = HandlerList()

class EndMiniGameEvent(miniGame: MiniGame<*>) : MiniGameEvent(miniGame) {
    companion object {
        @JvmStatic
        fun getHandlerList() = handlerList
    }

    override fun getHandlers() = handlerList
}