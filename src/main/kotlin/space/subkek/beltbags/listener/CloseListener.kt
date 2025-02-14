package space.subkek.beltbags.listener

import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import space.subkek.beltbags.BeltBagInventory
import space.subkek.beltbags.BeltBags

class CloseListener : Listener {
  @EventHandler
  private fun on(event: InventoryCloseEvent) {
    val player = event.player
    if (player !is Player) return
    val beltBagInv = event.inventory.holder
    if (beltBagInv !is BeltBagInventory) return

    run {
      val inv = beltBagInv.inv
      for (i in 0 until inv.size) {
        val item = inv.getItem(i) ?: continue
        if (item.type == Material.NETHERITE_LEGGINGS) {
          if (!item.persistentDataContainer.has(BeltBags.Keys.BELT_BAG_INV.key)) continue
          player.getWorld().dropItemNaturally(player.getLocation().add(BlockFace.UP.direction), item)
          inv.setItem(i, null)
        }
        if (item.type.name.contains("BUNDLE") || item.type.name.contains("SHULKER_BOX")) {
          player.getWorld().dropItemNaturally(player.getLocation().add(BlockFace.UP.direction), item)
          inv.setItem(i, null)
        }
      }
    }

    beltBagInv.save()
  }
}
