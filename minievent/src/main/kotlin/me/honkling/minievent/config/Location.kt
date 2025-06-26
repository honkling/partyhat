package me.honkling.minievent.config

import cc.ekblad.toml.model.TomlValue
import me.honkling.commonlib.config.decoder.Decoder
import me.honkling.commonlib.config.value
import org.bukkit.Bukkit
import org.bukkit.Location
import kotlin.reflect.KType

val location: Decoder = Location::class to { type: KType, it: TomlValue ->
    if (it !is TomlValue.Map)
        it
    else {
        val (properties) = it

        if (!properties.keys.containsAll(listOf("x", "y", "z")))
            it
        else
            Location(
                properties["world"]?.value<String>()?.let { Bukkit.getWorld(it) },
                properties["x"]!!.value(),
                properties["y"]!!.value(),
                properties["z"]!!.value(),
                properties["yaw"]?.value<Double>()?.toFloat() ?: 0f,
                properties["pitch"]?.value<Double>()?.toFloat() ?: 0f
            )
    }
}