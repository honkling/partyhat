package me.honkling.minievent.minigame

import me.honkling.minievent.config.MapsToml
import me.honkling.minievent.config.mapsToml
import me.honkling.minievent.feature.BossBarTimer
import me.honkling.minievent.lib.mm
import me.honkling.partyhat.PartyHat
import me.honkling.partyhat.feature.MapDistribution
import me.honkling.partyhat.feature.TimeLimit
import me.honkling.partyhat.feature.TimeUnit
import me.honkling.partyhat.minigame.MiniGame
import me.honkling.partyhat.team.FFADistributor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.inventory.ItemStack

private val bow = ItemStack(Material.BOW)
    .also {
        it.addEnchantment(Enchantment.INFINITY, 1)
    }

// 'One in the Chamber' is a minigame where everybody gets a bow.
// If you are shot, you respawn and the attacker is given a point.
// Player with most points after a set amount of time wins.

// Extends `Listener`, indicating to PartyHat that this minigame has events
// which should be registered only for the duration of the minigame.
class OneInTheChamber(partyHat: PartyHat) : MiniGame<MapsToml.OITC>("oitc", partyHat), Listener {
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
        // `players` is a property that takes the members
        // of all teams and flattens into one list of players.
        for (player in players) {
            player.give(bow)
            player.give(ItemStack(Material.ARROW))
        }
    }

    override fun deinitialize() {
        val winner = players.maxBy { it.score }
        Bukkit.broadcast("<s>${winner.name}</s> is the winner!".mm)
    }

    @EventHandler
    fun onProjectileHit(event: ProjectileHitEvent) {
        // Erase projectile after 3 seconds if it hit the ground
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            partyHat.plugin,
            event.entity::remove,
            20L * 3
        )

        val attacker = event.entity.shooter as? Player ?: return
        val victim = event.hitEntity as? Player ?: return
        val players = players

        if (attacker !in players && victim !in players)
            return // Event isn't relevant to this minigame

        // Kill victim, increment attacker's score
        victim.health = 0.0
        attacker.playSound(attacker.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f)
        attacker.score++
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        if (event.player !in players)
            return

        val attacker = event.damageSource as? Player ?: return
        val victim = event.player

        event.deathMessage("☠ <s>${attacker.name}</s> -> <s>${victim.name}</s>".mm)
    }

    // This distributes players into teams.
    // `FFADistributor` gives each player their own team.
    override fun teamDistributor() = FFADistributor

    // A list of valid 'playgrounds' (in effect, maps) that can be used.
    // In my case, I just have it hooked up to the maps stored in config.
    override fun playgrounds() = mapsToml.oitc
}