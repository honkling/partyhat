package me.honkling.partyhat.event

import me.honkling.partyhat.minigame.MiniGame
import org.bukkit.event.Event

abstract class MiniGameEvent(val miniGame: MiniGame<*>) : Event()