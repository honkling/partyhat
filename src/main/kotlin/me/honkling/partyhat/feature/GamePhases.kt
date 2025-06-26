package me.honkling.partyhat.feature

import me.honkling.partyhat.event.MiniGamePhaseChangeEvent
import me.honkling.partyhat.minigame.MiniGame
import org.bukkit.Bukkit
import kotlin.properties.Delegates

class GamePhases(
    vararg val phases: Pair<String, TimeLimit>
) : Feature {
    private var taskID by Delegates.notNull<Int>()
    var phase = 0

    override fun initialize(minigame: MiniGame<*>) {
        nextPhase(minigame)
    }

    override fun deinitialize(minigame: MiniGame<*>) {
        Bukkit.getScheduler().cancelTask(taskID)
    }

    private fun nextPhase(minigame: MiniGame<*>) {
        taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(minigame.partyHat.plugin, {
            if (phase + 1 >= phases.size)
                return@scheduleSyncDelayedTask minigame.end()

            phase++
            nextPhase(minigame)
            Bukkit.getPluginManager().callEvent(MiniGamePhaseChangeEvent(
                minigame,
                phases[phase - 1].first,
                phases[phase].first
            ))
        }, phases[phase].second.ticks())
    }
}