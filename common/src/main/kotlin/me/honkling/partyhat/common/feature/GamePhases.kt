package me.honkling.partyhat.common.feature

import me.honkling.partyhat.common.event.MiniGamePhaseChangeEventRef
import me.honkling.partyhat.common.minigame.AbstractMiniGame
import me.honkling.partyhat.common.platform.TaskAdapter

class GamePhases(
    vararg val phases: Pair<String, TimeLimit>
) : AbstractFeature<AbstractMiniGame<*, *, *>> {
    private lateinit var task: TaskAdapter
    var phase = 0

    override fun initialize(minigame: AbstractMiniGame<*, *, *>) {
        nextPhase(minigame)
    }

    override fun deinitialize(minigame: AbstractMiniGame<*, *, *>) {
        task.cancel()
    }

    private fun nextPhase(minigame: AbstractMiniGame<*, *, *>) {
        task = minigame.partyHat.scheduler.delay(phases[phase].second.ticks()) {
            if (phase + 1 >= phases.size)
                return@delay minigame.end()

            phase++
            nextPhase(minigame)

            minigame.partyHat.eventManager.instantiateEvent<MiniGamePhaseChangeEventRef>(
                minigame,
                phases[phase - 1].first,
                phases[phase].first
            ).fireEvent()
        }
    }
}