package me.honkling.partyhat

import kotlinx.coroutines.CoroutineScope
import me.honkling.commando.spigot.SpigotCommando
import me.honkling.partyhat.loader.ScriptLoader
import me.honkling.partyhat.runtime.modules
import org.bukkit.plugin.java.JavaPlugin
import kotlin.coroutines.EmptyCoroutineContext

val instance = JavaPlugin.getPlugin(PartyHat::class.java)
val scriptsFolder = instance.dataFolder.resolve("scripts/")
val scope = CoroutineScope(EmptyCoroutineContext)

class PartyHat : JavaPlugin() {
    val scriptLoader = ScriptLoader()

    override fun onEnable() {
        scriptsFolder.mkdirs()

        val commando = SpigotCommando(this)
        commando.register("me.honkling.partyhat", "command")

        val result = scriptLoader.loadAll()
        componentLogger.info(scriptLoader.displayResultMap(scriptsFolder, result))
    }

    override fun onDisable() {
        for (module in modules)
            module.cleanUp()
    }
}
