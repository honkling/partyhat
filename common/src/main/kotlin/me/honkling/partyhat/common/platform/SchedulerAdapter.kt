package me.honkling.partyhat.common.platform

interface SchedulerAdapter {
    fun delay(ticks: Long, block: () -> Unit): TaskAdapter
    fun repeat(delay: Long, interval: Long, block: () -> Unit): TaskAdapter
    fun runTask(block: () -> Unit): TaskAdapter
}

interface TaskAdapter {
    fun cancel()
}