package me.honkling.partyhat.team

import org.bukkit.entity.Player

/**
 * Accepts a set of team identifiers, and distributes
 * all players evenly into each team.
 */
class EvenDistributor(val teams: List<String>) : TeamDistributor {
    override fun distribute(players: List<Player>)
        = players.chunked(teams.size)
            .mapIndexed { i, v -> Team(teams[i], v.toMutableList()) }
}