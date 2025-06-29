package me.honkling.partyhat.event

import me.honkling.partyhat.minigame.MiniGame

class MiniGamePhaseChangeEvent(miniGame: MiniGame<*>, val oldPhase: String, val phase: String) : MiniGameEvent(miniGame)