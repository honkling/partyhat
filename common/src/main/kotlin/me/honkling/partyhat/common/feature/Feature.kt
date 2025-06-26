package me.honkling.partyhat.common.feature

import me.honkling.partyhat.common.minigame.AbstractMiniGame

interface AbstractFeature<M : AbstractMiniGame<*, *, *>> {
    fun initialize(minigame: M)
    fun deinitialize(minigame: M)
}