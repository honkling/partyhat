@file:Command("start", permission = "minievent.start")

package me.honkling.minievent.command

import me.honkling.commando.common.command.node.ParameterNode
import me.honkling.commando.minestom.command.Command
import me.honkling.minievent.minigame.OneInTheChamber
import me.honkling.partyhat.minigame.MiniGame
import net.kyori.adventure.text.Component
import net.minestom.server.command.CommandSender
import kotlin.reflect.KFunction0

private val miniGames = mutableMapOf<String, KFunction0<MiniGame<*>>>(
    "oitc" to ::OneInTheChamber
)

private fun start(sender: CommandSender, miniGame: String) {
    val constructor = miniGames[miniGame]
        ?: return sender.sendMessage(Component.text("Invalid minigame."))

    val miniGame = constructor()
    miniGame.start()
}

private fun `start$complete`(_sender: CommandSender, _node: ParameterNode<Command>, input: String): List<String> {
    return miniGames.keys.filter { input.lowercase() in it }
}