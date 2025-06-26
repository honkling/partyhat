package me.honkling.partyhat.paper.platform

import me.honkling.partyhat.common.platform.PlayerAdapter
import org.bukkit.entity.Player

class PlayerAdapter(private val player: Player) : PlayerAdapter<Player> {
    override fun accessor() = player
    override fun clearInventory() {
        player.inventory.clear()
    }
}