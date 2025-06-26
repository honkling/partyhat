package me.honkling.partyhat.common.team

import me.honkling.partyhat.common.platform.PlayerAdapter

/**
 * Distributes each person into their own respective team.
 * In effect, there are no teams, hence 'FFA.'
 */
object FFADistributor : TeamDistributor {
    override fun <Player : Any> distribute(players: List<PlayerAdapter<Player>>)
        = players.map { Team(null, mutableListOf(it.accessor())) }
}