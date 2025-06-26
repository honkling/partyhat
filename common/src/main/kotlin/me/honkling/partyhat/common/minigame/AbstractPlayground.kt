package me.honkling.partyhat.common.minigame

import java.util.concurrent.CompletableFuture

interface AbstractPlayground<World : Any> {
    fun world(): World
    fun initialize(): CompletableFuture<Nothing?>
    fun deinitialize()
}