package me.honkling.minievent.feature

import me.honkling.minievent.instance
import me.honkling.minievent.lib.mm
import me.honkling.partyhat.event.MiniGamePhaseChangeEvent
import me.honkling.partyhat.feature.Feature
import me.honkling.partyhat.feature.GamePhases
import me.honkling.partyhat.feature.TimeLimit
import me.honkling.partyhat.minigame.MiniGame
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.bossbar.BossBar.bossBar
import net.kyori.adventure.bossbar.BossBar.Color.WHITE
import net.kyori.adventure.bossbar.BossBar.Overlay.PROGRESS
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import kotlin.properties.Delegates

class BossBarTimer : Feature, Listener {
    private var taskID by Delegates.notNull<Int>()
    private var phaseBar: BossBar? = null
    private var timeLimitBar: BossBar? = null
    var timeElapsed = 0

    override fun initialize(minigame: MiniGame<*>) {
        val phaseFeature = minigame.features.filterIsInstance<GamePhases>().firstOrNull()
        val timeLimitFeature = minigame.features.filterIsInstance<TimeLimit>().firstOrNull()

        phaseBar = phaseFeature?.let { bossBar(Component.empty(), 0f, WHITE, PROGRESS) }
        timeLimitBar = timeLimitFeature?.let { bossBar(Component.empty(), 0f, WHITE, PROGRESS) }

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, {
            if (phaseBar != null) {
                val (name, time) = phaseFeature!!.phases[phaseFeature.phase]
                val total = (time.ticks() / 20.0).toLong()
                val seconds = total - timeElapsed

                phaseBar!!.name("<b>${name}</b> <gray>(${formatSeconds(seconds)})</gray>".mm)
                phaseBar!!.progress(seconds / total.toFloat())
            }

            if (timeLimitBar != null) {
                val total = (timeLimitFeature!!.ticks() / 20.0).toLong()
                val seconds = total - timeElapsed
                val timer = formatSeconds(seconds)//.replace(":", "</s>:<s>")

                timeLimitBar!!.name("Time left: <s>$timer</s>".mm)
                timeLimitBar!!.progress(seconds / total.toFloat())
            }

            timeElapsed++
        }, 0L, 20L)

        val audience = Audience.audience(*minigame.players.toTypedArray())
        timeLimitBar?.addViewer(audience)
        phaseBar?.addViewer(audience)
    }

    override fun deinitialize(minigame: MiniGame<*>) {
        Bukkit.getScheduler().cancelTask(taskID)

        phaseBar?.viewers()?.forEach { phaseBar!!.removeViewer(it as Audience) }
        timeLimitBar?.viewers()?.forEach { timeLimitBar!!.removeViewer(it as Audience) }
    }

    private fun formatSeconds(timer: Long): String {
        val minutes = timer / 60
        val seconds = timer - minutes * 60

        return minutes.toString().padStart(2, '0') +
                ":" + seconds.toString().padStart(2, '0')
    }

    @EventHandler
    fun onPhaseChange(event: MiniGamePhaseChangeEvent) {
        timeElapsed = 0
    }
}