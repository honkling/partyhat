package me.honkling.partyhat.paper.event

import me.honkling.partyhat.common.event.MiniGamePhaseChangeEventRef
import me.honkling.partyhat.paper.minigame.MiniGame
import org.bukkit.event.HandlerList

private val handlerList = HandlerList()

class MiniGamePhaseChangeEvent(
    miniGame: MiniGame<*>,
    override val oldPhase: String,
    override val phase: String
) : MiniGameEvent(miniGame), MiniGamePhaseChangeEventRef {
    companion object {
        @JvmStatic
        fun getHandlerList() = handlerList
    }

    override fun getHandlers() = handlerList
}