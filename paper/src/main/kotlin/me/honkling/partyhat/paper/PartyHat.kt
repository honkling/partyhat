package me.honkling.partyhat.paper

import me.honkling.partyhat.common.AbstractPartyHat
import me.honkling.partyhat.common.event.EndMiniGameEventRef
import me.honkling.partyhat.common.event.MiniGamePhaseChangeEventRef
import me.honkling.partyhat.common.event.StartMiniGameEventRef
import me.honkling.partyhat.paper.event.EndMiniGameEvent
import me.honkling.partyhat.paper.event.MiniGamePhaseChangeEvent
import me.honkling.partyhat.paper.event.StartMiniGameEvent
import me.honkling.partyhat.paper.platform.EventManager
import me.honkling.partyhat.paper.platform.Scheduler
import org.bukkit.plugin.java.JavaPlugin

class PartyHat(val plugin: JavaPlugin) : AbstractPartyHat() {
    override val eventManager = EventManager(plugin)
    override val scheduler = Scheduler(plugin)

    init {
        eventManager.registerEvent<StartMiniGameEventRef>(::StartMiniGameEvent)
        eventManager.registerEvent<EndMiniGameEventRef>(::EndMiniGameEvent)
        eventManager.registerEvent<MiniGamePhaseChangeEventRef>(::MiniGamePhaseChangeEvent)
    }
}