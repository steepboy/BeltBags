package space.subkek.beltbags.listener

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import space.subkek.beltbags.util.BBUtil

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
    val uuid = BBUtil.getUUID(item) ?: return

    BBUtil.open(uuid, player, item.type)

    event.isCancelled = true
  }
}
