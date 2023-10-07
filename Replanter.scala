package dev.ekuinox.miharu.planter

import java.util.logging.Logger
import scala.util.control.Exception._
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.block.Block
import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.block.data.Ageable
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import dev.ekuinox.miharu.Miharu

class Replanter(log: Logger, miharu: Miharu) extends Listener:
  log.info("Replanter enabled")

  @EventHandler
  def handleBlockBreak(event: BlockBreakEvent): Unit =
    val planted = event.getBlock.getType
    val seed = Miharu.revItems.get(planted) match
      case Some(b) => b
      case _       => return

    val player = event.getPlayer
    val inventory = player.getInventory
    val slot = inventory.first(seed)
    if slot < 0 then return
    ReplantTask(inventory.getItem(slot), event.getBlock, planted)
      .runTaskLater(miharu, 10)

class ReplantTask(stack: ItemStack, block: Block, planted: Material)
    extends BukkitRunnable:
  def run(): Unit =
    stack.setAmount(stack.getAmount - 1)
    block.setType(planted)

    val data = allCatch opt { block.getBlockData.asInstanceOf[Ageable] } match
      case Some(data) => data
      case _          => return
    data.setAge(0)
    block.setBlockData(data)
