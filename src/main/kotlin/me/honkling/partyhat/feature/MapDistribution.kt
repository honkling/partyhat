package me.honkling.partyhat.feature

import me.honkling.partyhat.event.EventNodeContainer
import me.honkling.partyhat.minigame.MiniGame
import me.honkling.partyhat.minigame.Playground
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerRespawnEvent

interface MapDistributionPlayground : Playground {
    fun mapDistributionPoints(): List<Pos>
}

class MapDistribution(
    val playground: MapDistributionPlayground,
    val allowRespawns: Boolean = true
) : Feature, EventNodeContainer {
    override val eventNode = EventNode.all("map-distribution")

    override fun initialize(minigame: MiniGame<*>) {
        val points = playground.mapDistributionPoints()

        eventNode.addListener(PlayerRespawnEvent::class.java) { event ->
            if (allowRespawns)
                event.respawnPosition = points.random()
        }

        for ((index, player) in minigame.players.withIndex()) {
            val location = points[index.mod(points.size)]
            player.setInstance(playground.instanceContainer(), location).join()
        }
    }

    override fun deinitialize(minigame: MiniGame<*>) {}
}