package me.honkling.partyhat.common.minigame

import me.honkling.partyhat.common.AbstractPartyHat
import me.honkling.partyhat.common.event.EndMiniGameEventRef
import me.honkling.partyhat.common.event.StartMiniGameEventRef
import me.honkling.partyhat.common.feature.AbstractFeature
import me.honkling.partyhat.common.platform.PlayerAdapter
import me.honkling.partyhat.common.team.Team
import me.honkling.partyhat.common.team.TeamDistributor
import kotlin.reflect.KProperty

/**
 * Extend this base class to implement a minigame.
 *
 * If your class implements Listener, then they will
 * be dynamically registered and unregistered per the lifecycle of the minigame.
 */
abstract class AbstractMiniGame<
        PartyHat : AbstractPartyHat,
        Player : Any,
        MiniGame : AbstractMiniGame<PartyHat, Player, MiniGame, Feature, PlaygroundImpl>,
        Feature : AbstractFeature<*>,
        PlaygroundImpl : AbstractPlayground<*>
>(val identifier: String, val partyHat: PartyHat) {
    protected abstract val candidatePlayers: List<PlayerAdapter<Player>>

    val players: List<Player>
        get() = teams.map(Team<Player>::members).flatten()

    val features = mutableSetOf<Feature>()
    val playground = playgrounds().random()
    private lateinit var playerAdapters: List<PlayerAdapter<Player>>
    lateinit var teams: List<Team<Player>>; private set

    protected abstract fun initialize()
    protected abstract fun deinitialize()
    protected abstract fun teamDistributor(): TeamDistributor
    protected abstract fun playgrounds(): List<PlaygroundImpl>

    fun start() {
        playground.initialize().thenRun {
            partyHat.scheduler.runTask {
                playerAdapters = candidatePlayers
                teams = teamDistributor().distribute(playerAdapters)

                for (player in playerAdapters)
                    player.clearInventory()

                partyHat.eventManager.tryRegisterListeners(this)

                for (feature in features) {
                    feature.initialize(this as MiniGame)
                    partyHat.eventManager.tryRegisterListeners(feature)
                }

                initialize()
                partyHat.eventManager.instantiateEvent<StartMiniGameEventRef>(this).fireEvent()
            }
        }
    }

    fun end() {
        partyHat.eventManager.instantiateEvent<EndMiniGameEventRef>(this).fireEvent()

        for (player in playerAdapters)
            player.clearInventory()

        partyHat.eventManager.tryUnregisterListeners(this)

        for (feature in features) {
            feature.deinitialize(this as MiniGame)
            partyHat.eventManager.tryUnregisterListeners(feature)
        }

        deinitialize()
        playground.deinitialize()
    }

    protected fun addFeature(feature: Feature) {
        features += feature
    }

    protected inline fun <reified T: Any> data(defaultValue: T): DataHolder<Player, T> {
        return DataHolder(defaultValue)
    }
}

class DataHolder<Player : Any, T : Any>(private val defaultValue: T) {
    private val valueMap = mutableMapOf<Player, T>()

    operator fun getValue(thisRef: Player, property: KProperty<*>): T {
        return valueMap.getOrDefault(thisRef, defaultValue)
    }

    operator fun setValue(thisRef: Player, property: KProperty<*>, value: T) {
        valueMap[thisRef] = value
    }
}