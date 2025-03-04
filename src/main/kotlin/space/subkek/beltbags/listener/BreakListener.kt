package space.subkek.beltbags.listener

import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemBreakEvent
import space.subkek.beltbags.BeltBagInventory
import space.subkek.beltbags.BeltBags
import java.util.*

class BreakListener : Listener {
  @EventHandler
  private fun on(event: PlayerItemBreakEvent) {
    val leggings = event.brokenItem
    if (leggings.type != Material.NETHERITE_LEGGINGS) return

    val data = leggings.persistentDataContainer
    val stringUUID = data.get(BeltBags.Keys.BELT_BAG_INV.key, BeltBags.Keys.BELT_BAG_INV.dataType) ?: return
    val uuid = UUID.fromString(stringUUID)

    val holder: BeltBagInventory = BeltBags.plugin.data.getBeltBagInventory(uuid)

    val inv = holder.inventory
    val player = event.player
    for (i in 0 until inv.size) {
      val item = inv.getItem(i) ?: continue
      player.world.dropItemNaturally(player.location.add(BlockFace.UP.direction), item)
      inv.setItem(i, null)
    }

    holder.save()
  }
}
