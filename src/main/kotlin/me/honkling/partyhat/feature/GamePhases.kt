package me.honkling.partyhat.feature

import me.honkling.partyhat.event.MiniGamePhaseChangeEvent
import me.honkling.partyhat.minigame.MiniGame
import net.minestom.server.MinecraftServer
import net.minestom.server.timer.Task
import net.minestom.server.timer.TaskSchedule

class GamePhases(
    vararg val phases: Pair<String, TimeLimit>
) : Feature {
    private lateinit var task: Task
    var phase = 0

    override fun initialize(minigame: MiniGame<*>) {
        nextPhase(minigame)
    }

    override fun deinitialize(minigame: MiniGame<*>) {
        task.cancel()
    }

    private fun nextPhase(minigame: MiniGame<*>) {
        task = MinecraftServer.getSchedulerManager().scheduleTask({
            if (phase + 1 >= phases.size)
                minigame.end()
            else {
                phase++
                nextPhase(minigame)
                MinecraftServer.getGlobalEventHandler().call(MiniGamePhaseChangeEvent(
                    minigame,
                    phases[phase - 1].first,
                    phases[phase].first
                ))
            }

            TaskSchedule.stop()
        }, TaskSchedule.tick(phases[phase].second.ticks()))
    }
}