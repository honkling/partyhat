package me.honkling.partyhat.common.event

interface MiniGamePhaseChangeEventRef : MiniGameEventRef {
    val oldPhase: String
    val phase: String
}