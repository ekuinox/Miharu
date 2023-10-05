//> using repository https://hub.spigotmc.org/nexus/content/repositories/snapshots/
//> using dep org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT
//> using resourceDir ./resources
//> using scala "3.3.0"

package dev.ekuinox.Miharu

import org.bukkit.plugin.java.JavaPlugin

class Miharu extends JavaPlugin:
  override def onEnable(): Unit =
    getLogger().info("Miharu enabled")

  override def onDisable(): Unit =
    getLogger().info("Miharu disabled")
