package me.honkling.minievent.config

import cc.ekblad.toml.tomlMapper
import me.honkling.commonlib.config.decoder.use
import me.honkling.commonlib.config.getAndMapConfig
import me.honkling.minievent.minigame.WorldPlayground
import me.honkling.partyhat.paper.feature.MapDistributionPlayground
import org.bukkit.Location
import java.io.File

lateinit var mapsToml: MapsToml; private set

data class MapsToml(
    val spawn: Location,
    val oitc: List<OITC>
) {
    data class OITC(
        val schematic: String,
        val points: List<Location>
    ) : WorldPlayground(File(schematic)), MapDistributionPlayground {
        override fun mapDistributionPoints() = points
    }
}

fun reloadMapsToml() {
    val mapper = tomlMapper {
        use(location)
    }

    mapsToml = getAndMapConfig("maps.toml", mapper)
}