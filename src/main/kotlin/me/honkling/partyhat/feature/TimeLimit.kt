package me.honkling.partyhat.feature

import me.honkling.partyhat.minigame.MiniGame
import org.bukkit.Bukkit
import kotlin.properties.Delegates

enum class TimeUnit(val multiplier: Int) {
    Ticks(1),
    Seconds(20),
    Minutes(1200)
}

class TimeLimit(val value: Long, val unit: TimeUnit) : Feature {
    private var taskID by Delegates.notNull<Int>()

    override fun initialize(minigame: MiniGame<*>) {
        taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(minigame.partyHat.plugin, {
            minigame.end()
        }, ticks())
    }

    override fun deinitialize(minigame: MiniGame<*>) {
        Bukkit.getScheduler().cancelTask(taskID)
    }

    fun ticks() = value * unit.multiplier
}