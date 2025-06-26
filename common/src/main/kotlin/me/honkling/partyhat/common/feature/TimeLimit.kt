package me.honkling.partyhat.common.feature

import me.honkling.partyhat.common.minigame.AbstractMiniGame
import me.honkling.partyhat.common.platform.TaskAdapter

enum class TimeUnit(val multiplier: Int) {
    Ticks(1),
    Seconds(20),
    Minutes(1200)
}

class TimeLimit(val value: Long, val unit: TimeUnit) : AbstractFeature<AbstractMiniGame<*, *, *>> {
    private lateinit var task: TaskAdapter

    override fun initialize(minigame: AbstractMiniGame<*, *, *>) {
        task = minigame.partyHat.scheduler.delay(ticks()) {
            minigame.end()
        }
    }

    override fun deinitialize(minigame: AbstractMiniGame<*, *, *>) {
        task.cancel()
    }

    fun ticks() = value * unit.multiplier
}