package me.honkling.partyhat.feature

import me.honkling.partyhat.minigame.MiniGame
import me.honkling.partyhat.minigame.Playground

interface Feature {
    fun initialize(minigame: MiniGame<*>)
    fun deinitialize(minigame: MiniGame<*>)
}