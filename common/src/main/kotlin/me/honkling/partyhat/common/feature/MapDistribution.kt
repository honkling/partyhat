package me.honkling.partyhat.common.feature

import me.honkling.partyhat.common.minigame.AbstractMiniGame
import me.honkling.partyhat.common.minigame.AbstractPlayground

interface AbstractMapDistributionPlayground<World : Any, Location : Any> : AbstractPlayground<World> {
    fun mapDistributionPoints(): List<Location>
}

abstract class AbstractMapDistribution<World : Any, Location : Any, M : AbstractMiniGame<*, *, *, *, *>>(
    val playground: AbstractMapDistributionPlayground<World, Location>,
    val allowRespawns: Boolean = true
) : AbstractFeature<M> {
    val cachedPoints = mutableListOf<Location>()

    override fun deinitialize(minigame: M) {}
}