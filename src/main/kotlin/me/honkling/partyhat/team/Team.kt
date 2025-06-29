package me.honkling.partyhat.team

import net.minestom.server.entity.Player

data class Team(
    val identifier: String?,
    val members: MutableList<Player>
)
