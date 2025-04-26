@file:Command("scripts", "scp", permission = "partyhat.scripts")

package me.honkling.partyhat.command

import kotlinx.coroutines.launch
import me.honkling.commando.spigot.command.Command
import me.honkling.partyhat.instance
import me.honkling.partyhat.scope
import me.honkling.partyhat.scriptsFolder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.CommandSender

private fun reload(sender: CommandSender, name: String) {
    val file = if (name.endsWith(".kts")) scriptsFolder.resolve(name)
        else if (name == "all") scriptsFolder
        else listOf(
            scriptsFolder.resolve("$name.ws.kts"),
            scriptsFolder.resolve("$name.kts")
        ).find { it.exists() }

    if (file?.exists() != true)
        return sender.sendMessage(Component.text("That file doesn't exist.")
            .color(NamedTextColor.RED))

    scope.launch {
        val scriptLoader = instance.scriptLoader

        sender.sendMessage(Component.text("Reloading ")
            .color(NamedTextColor.RED)
            .append(Component.text(if (file == scriptsFolder) "all scripts" else file.relativeTo(scriptsFolder).name)
                .color(NamedTextColor.GOLD))
            .append(Component.text("...")))

        if (file.isDirectory) {
            val map = scriptLoader.loadAll(file)
            sender.sendMessage(scriptLoader.displayResultMap(file, map))
            return@launch
        }

        val result = scriptLoader.load(file)
        sender.sendMessage(scriptLoader.displayResult(file, result))
    }
}