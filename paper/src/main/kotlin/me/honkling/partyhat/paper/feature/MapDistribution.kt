package me.honkling.partyhat.paper.feature

import me.honkling.partyhat.common.feature.AbstractMapDistribution
import me.honkling.partyhat.common.feature.AbstractMapDistributionPlayground
import me.honkling.partyhat.paper.minigame.MiniGame
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

interface MapDistributionPlayground : AbstractMapDistributionPlayground<World, Location>

class MapDistribution(
    playground: MapDistributionPlayground,
    allowRespawns: Boolean = true
) : AbstractMapDistribution<World, Location, MiniGame<*>>(playground, allowRespawns), Listener {
    override fun initialize(minigame: MiniGame<*>) {
        val points = playground.mapDistributionPoints()

        for ((index, player) in minigame.players.withIndex()) {
            val location = points[index.mod(points.size)].clone()
            location.world = playground.world()
            player.teleport(location)
            cachedPoints += location
        }
    }

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        if (allowRespawns)
            event.respawnLocation = cachedPoints.random()
    }
}