package me.honkling.partyhat.team

import org.bukkit.entity.Player

/**
 * Distributes players into X-player teams.
 */
class SizeDistributor(val size: Int) : TeamDistributor {
    override fun distribute(players: List<Player>)
        = players.chunked(size)
            .map { Team(null, it.toMutableList()) }
}