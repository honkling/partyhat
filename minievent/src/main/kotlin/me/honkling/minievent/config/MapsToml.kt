package me.honkling.minievent.config

import cc.ekblad.toml.tomlMapper
import me.honkling.minievent.minigame.InstancePlayground
import me.honkling.partyhat.feature.MapDistributionPlayground
import net.minestom.server.coordinate.Pos
import java.io.File

lateinit var mapsToml: MapsToml; private set

data class MapsToml(
    val spawn: Pos,
    val oitc: List<OITC>
) {
    data class OITC(
        val schematic: String,
        val points: List<Pos>
    ) : InstancePlayground(File(schematic)), MapDistributionPlayground {
        override fun mapDistributionPoints() = points
    }
}

fun reloadMapsToml() {
    val mapper = tomlMapper {
        use(location)
    }

    mapsToml = getAndMapConfig("maps.toml", mapper)
}