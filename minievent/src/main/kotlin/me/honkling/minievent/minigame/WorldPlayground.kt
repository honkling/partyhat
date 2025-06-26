package me.honkling.minievent.minigame

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import com.sk89q.worldedit.math.BlockVector3
import me.honkling.minievent.VoidGenerator
import me.honkling.minievent.config.mapsToml
import me.honkling.minievent.instance
import me.honkling.partyhat.minigame.Playground
import net.kyori.adventure.util.TriState
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.WorldType
import java.io.File
import java.util.UUID
import java.util.concurrent.CompletableFuture
import kotlin.concurrent.thread

open class WorldPlayground(val schematicFile: File) : Playground {
    lateinit var world: World; private set

    override fun world() = world

    override fun initialize(): CompletableFuture<Nothing?> {
        world = Bukkit.createWorld(WorldCreator.name("minigame-${UUID.randomUUID()}")
            .generator(VoidGenerator())
            .keepSpawnLoaded(TriState.FALSE))!!

        world.setGameRule(GameRule.SPAWN_CHUNK_RADIUS, 0)
        world.setGameRule(GameRule.KEEP_INVENTORY, true)
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true)
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false)
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
        world.isAutoSave = false

        val future = CompletableFuture<Nothing?>()
        thread {
            val format = ClipboardFormats.findByFile(schematicFile)!!
            val reader = format.getReader(schematicFile.inputStream())

            reader.use {
                val clipboard = it.read()
                Bukkit.getScheduler().runTask(instance, Runnable {
                    clipboard.paste(
                        BukkitAdapter.adapt(world),
                        BlockVector3.ZERO,
                        false,
                        false,
                        true,
                        null
                    )
                    future.complete(null)
                })
            }
        }

        File(world.name).deleteOnExit()
        return future
    }

    override fun deinitialize() {
        for (player in world.players)
            player.teleport(mapsToml.spawn)

        Bukkit.unloadWorld(world, false)
        File(world.name).deleteRecursively()
    }
}