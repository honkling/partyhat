@file:Command("gamemode", permission = "minievent.gamemode")

package me.honkling.minievent.command

import me.honkling.commando.minestom.command.Command
import me.honkling.minievent.lib.mm
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player

private fun gamemode(player: Player, gameMode: GameMode) {
    player.gameMode = gameMode
    player.sendMessage("<p>Set your game mode to <s>${gameMode.name}</s>.".mm)
}