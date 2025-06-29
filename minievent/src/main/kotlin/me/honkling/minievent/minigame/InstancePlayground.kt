package me.honkling.minievent.minigame

import dev.flavored.bamboo.SchematicReader
import me.honkling.minievent.config.mapsToml
import me.honkling.partyhat.minigame.Playground
import me.honkling.slate.defaultInstance
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.block.Block
import net.minestom.server.world.DimensionType
import java.io.File
import java.util.concurrent.CompletableFuture
import kotlin.concurrent.thread

val fullBrightDimension = MinecraftServer.getDimensionTypeRegistry().register(
    "minievent:full_bright",
    DimensionType.builder()
        .ambientLight(2.0f)
        .build()
)

open class InstancePlayground(val schematicFile: File) : Playground {
    lateinit var instanceContainer: InstanceContainer; private set

    override fun instanceContainer() = instanceContainer

    override fun initialize() {
        instanceContainer = MinecraftServer.getInstanceManager().createInstanceContainer(fullBrightDimension)
        instanceContainer.setGenerator {  }

        for (x in -1..1)
            for (z in -1..1)
                instanceContainer.loadChunk(x, z).join()

        val importer = SchematicReader()
        val schematic = importer.fromPath(schematicFile.toPath())
        val pos = Pos(
            -schematic.width.toDouble() / 2,
            -1.0,
            -schematic.length.toDouble() / 2
        )
        schematic.paste(instanceContainer, pos, true)
    }

    override fun deinitialize() {
        for (player in instanceContainer.players)
            player.setInstance(defaultInstance, mapsToml.spawn).join()

        MinecraftServer.getInstanceManager().unregisterInstance(instanceContainer)
    }
}