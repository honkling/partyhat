package me.honkling.partyhat.runtime

import me.honkling.partyhat.instance
import me.honkling.partyhat.scriptsFolder
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.EventExecutor
import java.io.File
import kotlin.reflect.KClass

val modules = ModuleRegistry()
val miniGames = ModuleRegistry(true)

class ModuleRegistry(private val isEphemeral: Boolean = false) : Iterable<Module> {
    private val modules = mutableMapOf<File, Module>()

    fun register(id: File): Module {
        if (!id.exists())
            throw IllegalArgumentException("File '${id.absolutePath}' does not exist for module")

        val module = modules[id]?.let {
            it.cleanUp()
            it
        } ?: Module(id, isEphemeral)

        modules[id] = module
        return module
    }

    fun unregister(id: File): Result<Nothing?> {
        val module = modules[id]
            ?: return Result.failure(IllegalArgumentException("There is no module with the id '$id'"))

        module.cleanUp()
        modules -= id
        return Result.success(null)
    }

    override fun iterator(): Iterator<Module> {
        return modules.values.iterator()
    }
}

class Module internal constructor(val file: File, val isEphemeral: Boolean) {
    class EventListener<T : Event>(
        val event: KClass<T>,
        val priority: EventPriority,
        val block: T.() -> Unit
    ) : Listener {
        fun register() {
            val pluginManager = Bukkit.getPluginManager()
            pluginManager.registerEvent(event.java, this, priority, EventExecutor { _, event ->
                block.invoke(event as T)
            }, instance)
        }

        fun unregister() {
            HandlerList.unregisterAll(this)
        }
    }

    val listeners = mutableListOf<EventListener<*>>()

    inline fun <reified T : Event> event(priority: EventPriority = EventPriority.NORMAL, noinline block: T.() -> Unit) {
        val listener = EventListener(T::class, priority, block)
        listeners += listener

        if (!isEphemeral)
            listener.register()
    }

    internal fun cleanUp() {
        for (listener in listeners)
            listener.unregister()

        listeners.clear()
    }
}