package me.honkling.partyhat.feature

import me.honkling.partyhat.minigame.MiniGame
import me.honkling.partyhat.minigame.Playground
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode

interface Feature {
    fun initialize(minigame: MiniGame<*>)
    fun deinitialize(minigame: MiniGame<*>)
}