package me.honkling.partyhat.minigame

import me.honkling.partyhat.event.EndMiniGameEvent
import me.honkling.partyhat.event.EventNodeContainer
import me.honkling.partyhat.event.StartMiniGameEvent
import me.honkling.partyhat.feature.Feature
import me.honkling.partyhat.team.Team
import me.honkling.partyhat.team.TeamDistributor
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import kotlin.reflect.KProperty

/**
 * Extend this base class to implement a minigame.
 *
 * If your class implements Listener, then they will
 * be dynamically registered and unregistered per the lifecycle of the minigame.
 */
abstract class MiniGame<PlaygroundImpl : Playground>(val identifier: String) {
    protected open val candidatePlayers: List<Player>
        get() = MinecraftServer.getConnectionManager().onlinePlayers.toList()

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
        val eventHandler = MinecraftServer.getGlobalEventHandler()

        playground.initialize()
        teams = teamDistributor().distribute(candidatePlayers)

        for (player in players)
            player.inventory.clear()

        if (this is EventNodeContainer)
            eventHandler.addChild(eventNode)

        for (feature in features) {
            feature.initialize(this)

            if (feature is EventNodeContainer)
                eventHandler.addChild(feature.eventNode)
        }

        initialize()
        eventHandler.call(StartMiniGameEvent(this))
    }

    fun end() {
        val eventHandler = MinecraftServer.getGlobalEventHandler()
        eventHandler.call(EndMiniGameEvent(this))

        for (player in players)
            player.inventory.clear()

        if (this is EventNodeContainer)
            eventHandler.removeChild(eventNode)

        for (feature in features) {
            feature.deinitialize(this)

            if (feature is EventNodeContainer)
                eventHandler.removeChild(feature.eventNode)
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