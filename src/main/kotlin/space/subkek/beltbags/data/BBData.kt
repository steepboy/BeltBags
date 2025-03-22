package space.subkek.beltbags.data

import org.bukkit.entity.Player
import space.subkek.beltbags.BeltBagInventory
import space.subkek.beltbags.BeltBags
import java.sql.SQLException
import java.util.*

class BBData {
  private val beltBagInventories: MutableMap<UUID, BeltBagInventory> = mutableMapOf()

  fun getBeltBagInventory(uuid: UUID, player: Player): BeltBagInventory {
    return beltBagInventories[uuid] ?: run {
      val result = loadBeltBag(uuid, player)
      beltBagInventories[uuid] = result
      return result
    }
  }

  private fun loadBeltBag(uuid: UUID, player: Player): BeltBagInventory {
    try {
      val slotItems = BeltBags.plugin.database.getBeltBagItems(uuid)
      val inventoryHolder = BeltBagInventory(uuid, player)

      for (slotItem in slotItems) {
        inventoryHolder.inventory.setItem(slotItem.slot, slotItem.item)
      }

      return inventoryHolder
    } catch (e: SQLException) {
      throw RuntimeException(e)
    }
  }
}
