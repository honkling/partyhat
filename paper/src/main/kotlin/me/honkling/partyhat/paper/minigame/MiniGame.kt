package me.honkling.partyhat.paper.minigame

import me.honkling.partyhat.common.minigame.AbstractMiniGame
import me.honkling.partyhat.common.minigame.AbstractPlayground
import me.honkling.partyhat.paper.PartyHat
import me.honkling.partyhat.paper.feature.Feature
import me.honkling.partyhat.paper.platform.PlayerAdapter
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Player

abstract class MiniGame<PlaygroundImpl : AbstractPlayground<World>>(
    identifier: String,
    partyHat: PartyHat
) : AbstractMiniGame<PartyHat, Player, MiniGame<PlaygroundImpl>, Feature, PlaygroundImpl>(identifier, partyHat) {
    override val candidatePlayers: List<PlayerAdapter>
        get() = Bukkit.getOnlinePlayers().map { PlayerAdapter(it) }
}