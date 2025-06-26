package me.honkling.partyhat.common.event

import me.honkling.partyhat.common.minigame.AbstractMiniGame
import me.honkling.partyhat.common.platform.EventAdapter

interface MiniGameEventRef : EventAdapter {
    val miniGame: AbstractMiniGame<*, *, *>
}