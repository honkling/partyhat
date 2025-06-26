package me.honkling.partyhat.feature

import me.honkling.partyhat.minigame.MiniGame
import me.honkling.partyhat.minigame.Playground
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

interface MapDistributionPlayground : Playground {
    fun mapDistributionPoints(): List<Location>
}

class MapDistribution(
    val playground: MapDistributionPlayground,
    val allowRespawns: Boolean = true
) : Feature, Listener {
    val cachedPoints = mutableListOf<Location>()

    override fun initialize(minigame: MiniGame<*>) {
        val points = playground.mapDistributionPoints()

        for ((index, player) in minigame.players.withIndex()) {
            val location = points[index.mod(points.size)].clone()
            location.world = playground.world()
            player.teleport(location)
            cachedPoints += location
        }
    }

    override fun deinitialize(minigame: MiniGame<*>) {}

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        if (allowRespawns)
            event.respawnLocation = cachedPoints.random()
    }
}