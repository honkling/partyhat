package me.honkling.partyhat.common.platform

interface PlayerAdapter<T : Any> {
    fun accessor(): T
    fun clearInventory()
}