package me.honkling.partyhat.feature

import me.honkling.partyhat.minigame.MiniGame
import net.minestom.server.MinecraftServer
import net.minestom.server.timer.Task
import net.minestom.server.timer.TaskSchedule

enum class TimeUnit(val multiplier: Int) {
    Ticks(1),
    Seconds(20),
    Minutes(1200)
}

class TimeLimit(val value: Int, val unit: TimeUnit) : Feature {
    private lateinit var task: Task

    override fun initialize(minigame: MiniGame<*>) {
        task = MinecraftServer.getSchedulerManager().scheduleTask({
            minigame.end()
            TaskSchedule.stop()
        }, TaskSchedule.tick(ticks()))
    }

    override fun deinitialize(minigame: MiniGame<*>) {
        task.cancel()
    }

    fun ticks() = value * unit.multiplier
}