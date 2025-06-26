package me.honkling.partyhat.team

import org.bukkit.entity.Player

/**
 * Distributes each person into their own respective team.
 * In effect, there are no teams, hence 'FFA.'
 */
object FFADistributor : TeamDistributor {
    override fun distribute(players: List<Player>)
        = players.map { Team(it.name, mutableListOf(it)) }
}