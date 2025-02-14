package space.subkek.beltbags.listener

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import space.subkek.beltbags.BeltBagInventory
import space.subkek.beltbags.BeltBags
import java.util.*

class OpenListener : Listener {
  @EventHandler
  private fun on(event: InventoryClickEvent) {
    val player = event.whoClicked
    if (player !is Player) return

    if (event.slotType != InventoryType.SlotType.ARMOR) return
    if (event.click != ClickType.DROP) return

    val clickedInv = event.clickedInventory
    if (clickedInv !== player.inventory) return

    val item = event.currentItem ?: return
    if (item.type != Material.NETHERITE_LEGGINGS) return

    val data = item.itemMeta.persistentDataContainer

    val stringUUID = data.get(BeltBags.Keys.BELT_BAG_INV.key, BeltBags.Keys.BELT_BAG_INV.dataType) ?: return
    val uuid = UUID.fromString(stringUUID)

    event.isCancelled = true

    val inv: BeltBagInventory = BeltBags.plugin.data.getBeltBagInventory(uuid)

    // Open Inventory to player
    // One tick delay is necessary to prevent client issues
    player.getScheduler().runDelayed(BeltBags.plugin, { player.openInventory(inv.inv) }, null, 1)
  }
}
