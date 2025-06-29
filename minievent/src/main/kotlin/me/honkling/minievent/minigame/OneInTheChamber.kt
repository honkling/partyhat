package me.honkling.minievent.minigame

import me.honkling.minievent.broadcastAudience
import me.honkling.minievent.config.MapsToml
import me.honkling.minievent.config.mapsToml
import me.honkling.minievent.feature.BossBarTimer
import me.honkling.minievent.lib.mm
import me.honkling.partyhat.event.EventNodeContainer
import me.honkling.partyhat.feature.MapDistribution
import me.honkling.partyhat.feature.TimeLimit
import me.honkling.partyhat.feature.TimeUnit
import me.honkling.partyhat.minigame.MiniGame
import me.honkling.partyhat.team.FFADistributor
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.minestom.server.entity.EntityProjectile
import net.minestom.server.entity.Player
import net.minestom.server.event.EventNode
import net.minestom.server.event.entity.projectile.ProjectileCollideWithBlockEvent
import net.minestom.server.event.entity.projectile.ProjectileCollideWithEntityEvent
import net.minestom.server.event.player.PlayerDeathEvent
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

// 'One in the Chamber' is a minigame where everybody gets a bow.
// If you are shot, you respawn and the attacker is given a point.
// Player with most points after a set amount of time wins.

// Extends `Listener`, indicating to PartyHat that this minigame has events
// which should be registered only for the duration of the minigame.
class OneInTheChamber : MiniGame<MapsToml.OITC>("oitc"), EventNodeContainer {
    override val eventNode = EventNode.all("oitc")
    // An ephemeral data value holding the player's score.
    // default value is `0`, so Player#score is an Int.
    private var Player.score by data(0)

    init {
        // Registers a 'time limit' feature, which ends the minigame
        // 3 minutes after it was started.
        addFeature(TimeLimit(3, TimeUnit.Minutes))

        // Registers a feature that distributes players randomly
        // across a list of locations defined by the playground (the map).
        addFeature(MapDistribution(playground))

        // Registers a feature that displays the current time limit on a boss bar.
        addFeature(BossBarTimer())
    }

    override fun initialize() {
        eventNode.addListener(PlayerDeathEvent::class.java) { event ->
            if (event.player !in players)
                return@addListener

            val attacker = event.player.lastDamageSource?.attacker as? Player ?: return@addListener
            val victim = event.player

            event.deathText = "☠ <s>${attacker.username}</s> -> <s>${victim.username}</s>".mm

            if (attacker.inventory.itemStacks.none { it.material() == Material.ARROW })
                attacker.inventory.addItemStack(ItemStack.of(Material.ARROW))

            if (attacker != victim)
                attacker.score++
        }

        eventNode.addListener(ProjectileCollideWithBlockEvent::class.java) { event ->
            event.entity.remove()
        }

        eventNode.addListener(ProjectileCollideWithEntityEvent::class.java) { event ->
            val attacker = (event.entity as EntityProjectile).shooter as? Player ?: return@addListener
            val victim = event.target as? Player ?: return@addListener
            val players = players

            if (attacker !in players && victim !in players)
                return@addListener // Event isn't relevant to this minigame

            // Kill victim, increment attacker's score
            victim.kill()
            attacker.playSound(Sound.sound {
                it.type(Key.key("minecraft", "block.note_block.pling"))
                it.pitch(2f)
            })
        }

        // `players` is a property that takes the members
        // of all teams and flattens into one list of players.
        for (player in players) {
            player.inventory.addItemStack(ItemStack.of(Material.STONE_SWORD))
            player.inventory.addItemStack(ItemStack.of(Material.BOW))
            player.inventory.addItemStack(ItemStack.of(Material.ARROW))
        }
    }

    override fun deinitialize() {
        val winner = players.maxBy { it.score }
        broadcastAudience.sendMessage("<s>${winner.username}</s> is the winner!".mm)
    }

    // This distributes players into teams.
    // `FFADistributor` gives each player their own team.
    override fun teamDistributor() = FFADistributor

    // A list of valid 'playgrounds' (in effect, maps) that can be used.
    // In my case, I just have it hooked up to the maps stored in config.
    override fun playgrounds() = mapsToml.oitc
}