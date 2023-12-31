package dev.ekuinox.miharu.filler

import java.util.logging.Logger

import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.Inventory
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.Material

import dev.ekuinox.miharu.Miharu

class Filler(log: Logger) extends Listener:
  log.info("Filler enabled")

  @EventHandler
  def handlePlayerInteract(event: PlayerInteractEvent): Unit =
    if event.getAction != Action.RIGHT_CLICK_BLOCK then return
    // HAND, OFF_HANDで2回発火してしまうのでHANDに絞る
    if Option(event.getHand) != Some(EquipmentSlot.HAND) then return

    val player = event.getPlayer
    val seed = player.getItemInHand.getType
    val planted = Miharu.items.get(seed)

    planted.zip(Option(event.getClickedBlock)) match
      case Some((planted, block)) if block.getType == Material.FARMLAND =>
        event.setCancelled(true)
        fillFarmLand(player.getInventory, block, seed, planted)
      case _ =>

/** 隣接している耕地全てに種を植える
  * @param inventory
  * @param baseBlock
  * @param seed
  *   種となる `Material`
  * @param planted
  *   植えた後の `Material`
  */
def fillFarmLand(
    inventory: Inventory,
    baseBlock: Block,
    seed: Material,
    planted: Material
): Unit =
  if plant(baseBlock, inventory, seed, planted) then
    fillNextFarmLand(
      inventory,
      baseBlock.getRelative(BlockFace.EAST),
      BlockFace.EAST,
      seed,
      planted
    )
    fillNextFarmLand(
      inventory,
      baseBlock.getRelative(BlockFace.WEST),
      BlockFace.WEST,
      seed,
      planted
    )
    fillFarmLand(
      inventory,
      baseBlock.getRelative(BlockFace.NORTH),
      seed,
      planted
    )
    fillFarmLand(
      inventory,
      baseBlock.getRelative(BlockFace.SOUTH),
      seed,
      planted
    )

/** 耕地に種を植えるのを隣へ隣へ続けて行う
  * @param inventory
  * @param farmland
  * @param face
  * @param seed
  * @param planted
  */
def fillNextFarmLand(
    inventory: Inventory,
    farmland: Block,
    face: BlockFace,
    seed: Material,
    planted: Material
): Unit =
  if plant(farmland, inventory, seed, planted) then
    fillNextFarmLand(inventory, farmland.getRelative(face), face, seed, planted)

/** 種を植える
  * @param farmland
  * @param inventory
  * @param seed
  * @param planted
  * @return
  *   植えられなければ `false` を返す
  */
def plant(
    farmland: Block,
    inventory: Inventory,
    seed: Material,
    planted: Material
): Boolean =
  val slot = inventory.first(seed)
  val target = farmland.getRelative(BlockFace.UP)
  if slot >= 0 && farmland.getType == Material.FARMLAND && target.getType == Material.AIR then
    target.setType(planted)
    val stack = inventory.getItem(slot)
    stack.setAmount(stack.getAmount - 1)
    true
  else false
