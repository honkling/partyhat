@file:Listener

package me.honkling.minievent.event

import me.honkling.commando.spigot.event.Listener
import org.bukkit.GameRule
import org.bukkit.event.world.WorldInitEvent

private fun onWorldInitialize(event: WorldInitEvent) {
    if (event.world.name.startsWith("minigame-"))
        event.world.setGameRule(GameRule.SPAWN_CHUNK_RADIUS, 0)
}