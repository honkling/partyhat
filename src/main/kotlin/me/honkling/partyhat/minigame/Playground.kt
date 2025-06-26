package me.honkling.partyhat.minigame

import org.bukkit.World
import java.util.concurrent.CompletableFuture

interface Playground {
    fun world(): World
    fun initialize(): CompletableFuture<Nothing?>
    fun deinitialize()
}