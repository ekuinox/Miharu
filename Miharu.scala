//> using repository https://hub.spigotmc.org/nexus/content/repositories/snapshots/
//> using dep org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT
//> using resourceDir ./resources
//> using scala "3.3.0"

package dev.ekuinox.miharu

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.block.Action
import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.block.Block

class Miharu extends JavaPlugin:
  override def onEnable(): Unit =
    getServer()
      .getPluginManager()
      .registerEvents(PlayerInteractEventListener(), this)
    getLogger().info("Miharu enabled")

  override def onDisable(): Unit =
    getLogger().info("Miharu disabled")

class PlayerInteractEventListener extends Listener:
  @EventHandler
  def onPlayerInteract(e: PlayerInteractEvent): Unit =
    if e.getAction != Action.RIGHT_CLICK_BLOCK then return
    if e.getPlayer.getInventory.getItemInMainHand.getType != Material.POTATO then
      return
    // HAND, OFF_HANDで2回発火してしまうのでHANDに絞る
    if Option(e.getHand) != Some(EquipmentSlot.HAND) then return

    Option(e.getClickedBlock) match
      case Some(block) if block.getType == Material.FARMLAND => fillFarmLand(e.getPlayer, block)
      case _ =>

def fillFarmLand(player: Player, baseBlock: Block): Unit =
  // 繋がっている農地全てに適用させたいけど、まあ後で考える
  val inventory = player.getInventory
  val mainHand = inventory.getItemInMainHand
  val north = baseBlock.getRelative(BlockFace.NORTH)
  if north.getType() == Material.FARMLAND then
    val amount = mainHand.getAmount
    val block = north.getRelative(BlockFace.UP)
    // 量のチェックがいるのかはわからないけど、上にブロックが何もないことを確認してから置くようにしたい
    if amount > 0 && block.getType == Material.AIR then
      mainHand.setAmount(amount - 1)
      block.setType(Material.POTATOES)
