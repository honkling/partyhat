package me.honkling.minievent

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.generator.ChunkGenerator
import java.util.Random

class VoidGenerator : ChunkGenerator() {
    override fun getFixedSpawnLocation(world: World, random: Random) = Location(world, 0.0, 0.0, 0.0)
    override fun canSpawn(world: World, x: Int, z: Int) = false
    override fun shouldGenerateDecorations() = false
    override fun shouldGenerateStructures() = false
    override fun shouldGenerateSurface() = false
    override fun shouldGenerateCaves() = false
    override fun shouldGenerateNoise() = false
    override fun shouldGenerateMobs() = false
}