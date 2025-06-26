package me.honkling.partyhat.common.team

import me.honkling.partyhat.common.platform.PlayerAdapter

/**
 * Handles how players should be divided into teams.
 * @see FFADistributor
 * @see EvenDistributor
 * @see SizeDistributor
 */
interface TeamDistributor {
    fun <Player : Any> distribute(players: List<PlayerAdapter<Player>>): List<Team<Player>>
}