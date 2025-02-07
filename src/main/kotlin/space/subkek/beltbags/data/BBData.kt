package space.subkek.beltbags.data

import space.subkek.beltbags.BeltBagInventory
import space.subkek.beltbags.BeltBags
import java.sql.SQLException
import java.util.*

class BBData {
  private val beltBagInventories: MutableMap<UUID, BeltBagInventory> = mutableMapOf()

  fun getBeltBagInventory(uuid: UUID): BeltBagInventory {
    return beltBagInventories[uuid] ?: run {
      val result = loadBeltBag(uuid)
      beltBagInventories[uuid] = result
      return result
    }
  }

  private fun loadBeltBag(uuid: UUID): BeltBagInventory {
    try {
      val slotItems = BeltBags.plugin.database.getBeltBagItems(uuid)
      val inventoryHolder = BeltBagInventory(uuid)

      for (slotItem in slotItems) {
        inventoryHolder.inventory.setItem(slotItem.slot, slotItem.item)
      }

      return inventoryHolder
    } catch (e: SQLException) {
      throw RuntimeException(e)
    }
  }
}
