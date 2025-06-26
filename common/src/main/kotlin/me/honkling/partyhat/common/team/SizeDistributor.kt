package me.honkling.partyhat.common.team

import me.honkling.partyhat.common.platform.PlayerAdapter

/**
 * Distributes players into X-player teams.
 */
class SizeDistributor(val size: Int) : TeamDistributor {
    override fun <Player : Any> distribute(players: List<PlayerAdapter<Player>>)
        = players.map { it.accessor() }
            .chunked(size)
            .map { Team(null, it.toMutableList()) }
}