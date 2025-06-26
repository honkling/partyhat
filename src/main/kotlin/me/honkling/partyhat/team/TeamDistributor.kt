package me.honkling.partyhat.team

import org.bukkit.entity.Player

/**
 * Handles how players should be divided into teams.
 * @see FFADistributor
 * @see EvenDistributor
 * @see SizeDistributor
 */
interface TeamDistributor {
    fun distribute(players: List<Player>): List<Team>
}