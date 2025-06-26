package me.honkling.partyhat.common.team

import me.honkling.partyhat.common.platform.PlayerAdapter

/**
 * Accepts a set of team identifiers, and distributes
 * all players evenly into each team.
 */
class EvenDistributor(val teams: List<String>) : TeamDistributor {
    override fun <Player : Any> distribute(players: List<PlayerAdapter<Player>>)
        = players.map { it.accessor() }
            .chunked(teams.size)
            .mapIndexed { i, v -> Team(teams[i], v.toMutableList()) }
}