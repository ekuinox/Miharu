//> using repository https://hub.spigotmc.org/nexus/content/repositories/snapshots/
//> using dep org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT
//> using resourceDir ./resources
//> using scala "3.3.0"

package dev.ekuinox.miharu

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.Material
import dev.ekuinox.miharu.filler.Filler
import dev.ekuinox.miharu.planter.Replanter

class Miharu extends JavaPlugin:
  override def onEnable(): Unit =
    val pluginManager = getServer.getPluginManager
    if getConfig.getBoolean("filler", false) then
      pluginManager.registerEvents(Filler(getLogger), this)
    if getConfig.getBoolean("replanter", false) then
      pluginManager.registerEvents(Replanter(getLogger, this), this)

object Miharu:
  // 種とその植えた後のブロックの対応
  val items = Map(
    Material.POTATO -> Material.POTATOES,
    Material.CARROT -> Material.CARROTS,
    Material.SWEET_BERRIES -> Material.SWEET_BERRY_BUSH,
    Material.WHEAT_SEEDS -> Material.WHEAT,
    Material.BEETROOT_SEEDS -> Material.BEETROOTS
  )

  val revItems = items.map((k, v) => (v, k)).toMap
