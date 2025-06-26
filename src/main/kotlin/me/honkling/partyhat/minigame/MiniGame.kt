package me.honkling.partyhat.minigame

import me.honkling.partyhat.PartyHat
import me.honkling.partyhat.event.EndMiniGameEvent
import me.honkling.partyhat.event.MiniGamePhaseChangeEvent
import me.honkling.partyhat.event.StartMiniGameEvent
import me.honkling.partyhat.feature.Feature
import me.honkling.partyhat.team.Team
import me.honkling.partyhat.team.TeamDistributor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import kotlin.reflect.KProperty

/**
 * Extend this base class to implement a minigame.
 *
 * If your class implements Listener, then they will
 * be dynamically registered and unregistered per the lifecycle of the minigame.
 */
abstract class MiniGame<PlaygroundImpl : Playground>(val identifier: String, val partyHat: PartyHat) {
    protected open val candidatePlayers: List<Player>
        get() = Bukkit.getOnlinePlayers().toList()

    val players: List<Player>
        get() = teams.map(Team::members).flatten()

    val features = mutableSetOf<Feature>()
    val playground = playgrounds().random()
    lateinit var teams: List<Team>; private set

    protected abstract fun initialize()
    protected abstract fun deinitialize()
    protected abstract fun teamDistributor(): TeamDistributor
    protected abstract fun playgrounds(): List<PlaygroundImpl>

    fun start() {
        playground.initialize().thenRun {
            Bukkit.getScheduler().runTask(partyHat.plugin, Runnable {
                val pluginManager = Bukkit.getPluginManager()
                teams = teamDistributor().distribute(candidatePlayers)

                for (player in players)
                    player.inventory.clear()

                if (this is Listener) {
                    println("Registering events for ${this::class.java.name}")
                    pluginManager.registerEvents(this, partyHat.plugin)
                }

                for (feature in features) {
                    feature.initialize(this)

                    if (feature is Listener)
                        pluginManager.registerEvents(feature, partyHat.plugin)
                }

                initialize()
                pluginManager.callEvent(StartMiniGameEvent(this))
            })
        }
    }

    fun end() {
        val pluginManager = Bukkit.getPluginManager()
        pluginManager.callEvent(EndMiniGameEvent(this))

        for (player in players)
            player.inventory.clear()

        if (this is Listener)
            HandlerList.unregisterAll(this)

        for (feature in features) {
            feature.deinitialize(this)

            if (feature is Listener)
                HandlerList.unregisterAll(feature)
        }

        deinitialize()
        playground.deinitialize()
    }

    protected fun addFeature(feature: Feature) {
        features += feature
    }

    protected inline fun <reified T: Any> data(defaultValue: T): DataHolder<T> {
        return DataHolder(defaultValue)
    }
}

class DataHolder<T : Any>(private val defaultValue: T) {
    private val valueMap = mutableMapOf<Player, T>()

    operator fun getValue(thisRef: Player, property: KProperty<*>): T {
        return valueMap.getOrDefault(thisRef, defaultValue)
    }

    operator fun setValue(thisRef: Player, property: KProperty<*>, value: T) {
        valueMap[thisRef] = value
    }
}