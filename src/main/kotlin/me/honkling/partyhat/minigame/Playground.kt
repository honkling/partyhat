package me.honkling.partyhat.minigame

import net.minestom.server.instance.InstanceContainer

interface Playground {
    fun instanceContainer(): InstanceContainer
    fun initialize()
    fun deinitialize()
}