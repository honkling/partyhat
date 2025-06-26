package me.honkling.partyhat.paper.platform

import me.honkling.partyhat.common.platform.AbstractEventManager
import me.honkling.partyhat.paper.PartyHat
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class EventManager(private val plugin: JavaPlugin) : AbstractEventManager() {
    override fun tryRegisterListeners(listener: Any) {
        if (listener is Listener)
            Bukkit.getPluginManager().registerEvents(listener, plugin)
    }

    override fun tryUnregisterListeners(listener: Any) {
        if (listener is Listener)
            HandlerList.unregisterAll(listener)
    }
}