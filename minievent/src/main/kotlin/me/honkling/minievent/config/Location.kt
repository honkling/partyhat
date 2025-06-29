package me.honkling.minievent.config

import cc.ekblad.toml.model.TomlValue
import net.minestom.server.coordinate.Pos
import kotlin.reflect.KType

val location: Decoder = Pos::class to { type: KType, it: TomlValue ->
    if (it !is TomlValue.Map)
        it
    else {
        val (properties) = it

        if (!properties.keys.containsAll(listOf("x", "y", "z")))
            it
        else
            Pos(
                properties["x"]!!.value(),
                properties["y"]!!.value(),
                properties["z"]!!.value(),
                properties["yaw"]?.value<Double>()?.toFloat() ?: 0f,
                properties["pitch"]?.value<Double>()?.toFloat() ?: 0f
            )
    }
}