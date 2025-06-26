package me.honkling.partyhat.common.platform

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

abstract class AbstractEventManager {
    private val implementationMap = mutableMapOf<KClass<out EventAdapter>, KFunction<EventAdapter>>()

    fun <E : EventAdapter> registerEvent(base: KClass<out E>, adapter: KFunction<E>) {
        implementationMap[base] = adapter
    }

    inline fun <reified E : EventAdapter> registerEvent(adapter: KFunction<E>) {
        registerEvent(E::class, adapter)
    }

    fun <E : EventAdapter> instantiateEvent(base: KClass<out E>, vararg args: Any?): E {
        @Suppress("UNCHECKED_CAST")
        return implementationMap[base]!!.call(*args) as E
    }

    inline fun <reified E : EventAdapter> instantiateEvent(vararg args: Any?): E {
        return instantiateEvent(E::class, *args)
    }

    abstract fun tryRegisterListeners(listener: Any)
    abstract fun tryUnregisterListeners(listener: Any)
}