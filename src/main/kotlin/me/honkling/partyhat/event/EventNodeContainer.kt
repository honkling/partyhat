package me.honkling.partyhat.event

import net.minestom.server.event.EventNode

interface EventNodeContainer {
    val eventNode: EventNode<*>
}