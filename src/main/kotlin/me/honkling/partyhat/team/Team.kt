package me.honkling.partyhat.team

import org.bukkit.entity.Player

data class Team(
    val identifier: String?,
    val members: MutableList<Player>
)
