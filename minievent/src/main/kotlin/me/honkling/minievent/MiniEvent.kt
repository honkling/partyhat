package me.honkling.minievent

import me.honkling.commando.spigot.SpigotCommando
import me.honkling.commonlib.CommonLib
import me.honkling.minievent.config.reloadMapsToml
import me.honkling.partyhat.PartyHat
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

val instance = JavaPlugin.getPlugin(MiniEvent::class.java)
val partyHat = PartyHat(instance)

class MiniEvent : JavaPlugin() {
    override fun onEnable() {
        for (world in Bukkit.getWorlds())
            if (world.name.startsWith("minigame-")) {
                Bukkit.unloadWorld(world, false)
                File(world.name).deleteRecursively()
            }

        CommonLib(this)

        val commando = SpigotCommando(this)
        commando.register("me.honkling.minievent", "command", "event")

        reloadMapsToml()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
