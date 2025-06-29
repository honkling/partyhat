package me.honkling.minievent

import me.honkling.commando.minestom.MinestomCommando
import me.honkling.minievent.config.reloadMapsToml
import me.honkling.minievent.minigame.fullBrightDimension
import me.honkling.slate.hasPermission
import me.honkling.slate.registerDefaultInstance
import net.kyori.adventure.audience.Audience
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.block.Block
import team.azalea.plugins.Plugin

val broadcastAudience: Audience
    get() = Audience.audience(MinecraftServer.getConnectionManager().onlinePlayers)

lateinit var instance: MiniEvent; private set

class MiniEvent : Plugin() {
    override fun setup() {
        fullBrightDimension
        dataFolder.mkdirs()
        instance = this

        val instanceContainer = MinecraftServer.getInstanceManager().createInstanceContainer(fullBrightDimension)
        instanceContainer.setGenerator { it.modifier().fillHeight(0, 40, Block.GRASS_BLOCK) }
        registerDefaultInstance(instanceContainer)

        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent::class.java) { event ->
            if (event.isFirstSpawn)
                event.player.teleport(Pos(0.0, 40.0, 0.0))
        }

        val commando = MinestomCommando(MiniEvent::class) { sender, _, permission ->
            sender.hasPermission(permission)
        }

        commando.register("me.honkling.minievent", "command", "event")

        reloadMapsToml()
    }

    override fun teardown() {

    }
}
