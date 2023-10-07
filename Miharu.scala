//> using repository https://hub.spigotmc.org/nexus/content/repositories/snapshots/
//> using dep org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT
//> using resourceDir ./resources
//> using scala "3.3.0"

package dev.ekuinox.miharu

import org.bukkit.plugin.java.JavaPlugin
import dev.ekuinox.miharu.filler.Filler

class Miharu extends JavaPlugin:
  override def onEnable(): Unit =
    val pluginManager = getServer.getPluginManager
    if getConfig.getBoolean("filler", false) then
      pluginManager.registerEvents(Filler(getLogger), this)
