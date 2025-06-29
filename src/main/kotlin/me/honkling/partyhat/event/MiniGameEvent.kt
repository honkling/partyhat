package me.honkling.partyhat.event

import me.honkling.partyhat.minigame.MiniGame
import net.minestom.server.event.Event

abstract class MiniGameEvent(val miniGame: MiniGame<*>) : Event