package me.honkling.partyhat.common

import me.honkling.partyhat.common.platform.AbstractEventManager
import me.honkling.partyhat.common.platform.SchedulerAdapter

abstract class AbstractPartyHat {
    abstract val eventManager: AbstractEventManager
    abstract val scheduler: SchedulerAdapter
}
