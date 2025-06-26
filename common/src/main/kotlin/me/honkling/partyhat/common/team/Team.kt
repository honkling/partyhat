package me.honkling.partyhat.common.team

data class Team<T : Any>(
    val identifier: String?,
    val members: MutableList<T>
)
