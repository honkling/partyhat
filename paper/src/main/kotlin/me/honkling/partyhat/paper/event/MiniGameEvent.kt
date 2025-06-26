package me.honkling.partyhat.paper.event

import me.honkling.partyhat.common.event.MiniGameEventRef
import me.honkling.partyhat.paper.minigame.MiniGame
import org.bukkit.event.Event

abstract class MiniGameEvent(override val miniGame: MiniGame<*>) : Event(), MiniGameEventRef {
    override fun fireEvent() {
        callEvent()
    }
}