package me.honkling.partyhat.paper.platform

import me.honkling.partyhat.common.platform.SchedulerAdapter
import me.honkling.partyhat.common.platform.TaskAdapter
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Scheduler(private val plugin: JavaPlugin) : SchedulerAdapter {
    private val scheduler = Bukkit.getScheduler()

    override fun delay(ticks: Long, block: () -> Unit): Task {
        val taskID = scheduler.scheduleSyncDelayedTask(plugin, block, ticks)
        return Task(taskID)
    }

    override fun repeat(
        delay: Long,
        interval: Long,
        block: () -> Unit
    ): Task {
        val taskID = scheduler.scheduleSyncRepeatingTask(plugin, block, delay, interval)
        return Task(taskID)
    }

    override fun runTask(block: () -> Unit): Task {
        val taskID = scheduler.runTask(plugin, block).taskId
        return Task(taskID)
    }
}

class Task(val id: Int) : TaskAdapter {
    override fun cancel() {
        Bukkit.getScheduler().cancelTask(id)
    }
}