package space.subkek.beltbags.listener

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

        if (item.persistentDataContainer.has(BeltBags.Keys.BELT_BAG_INV.key)) {
          player.world.dropItemNaturally(player.location.add(BlockFace.UP.direction), item)
          inv.setItem(i, null)
        } else if (item.type.name.contains("BUNDLE") || item.type.name.contains("SHULKER_BOX")) {
          player.world.dropItemNaturally(player.location.add(BlockFace.UP.direction), item)
          inv.setItem(i, null)
        }
      }
    }

    beltBagInv.save()
    if (beltBagInv.adminCreated) {
      beltBagInv.inv.viewers.forEach {
        it.closeInventory()
      }
      BeltBags.plugin.data.removeBeltBagInventory(beltBagInv.uuid)
    }
  }
}
