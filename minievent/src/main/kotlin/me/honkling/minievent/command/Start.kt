@file:Command("start", permission = "minievent.start")

package me.honkling.minievent.command

import me.honkling.commando.common.command.node.ParameterNode
import me.honkling.commando.spigot.command.Command
import me.honkling.minievent.minigame.OneInTheChamber
import me.honkling.minievent.partyHat
import me.honkling.partyhat.PartyHat
import me.honkling.partyhat.minigame.MiniGame
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender
import kotlin.reflect.KFunction1

private val miniGames = mutableMapOf<String, KFunction1<PartyHat, MiniGame<*>>>(
    "oitc" to ::OneInTheChamber
)

private fun start(sender: CommandSender, miniGame: String) {
    val constructor = miniGames[miniGame]
        ?: return sender.sendMessage(Component.text("Invalid minigame."))

    val miniGame = constructor(partyHat)
    miniGame.start()
}

private fun `start$complete`(_sender: CommandSender, _node: ParameterNode<Command>, input: String): List<String> {
    return miniGames.keys.filter { input.lowercase() in it }
}