package space.subkek.beltbags.data

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import space.subkek.beltbags.BeltBagInventory
import space.subkek.beltbags.BeltBags
import java.sql.SQLException
import java.util.*

class BBData {
  private val beltBagInventories: MutableMap<UUID, BeltBagInventory> = mutableMapOf()

  fun getBeltBagInventory(uuid: UUID, type: Material, location: Location): BeltBagInventory {
    return beltBagInventories[uuid] ?: run {
      val result = loadBeltBag(uuid, type, location)
      beltBagInventories[uuid] = result
      return result
    }
  }

  fun removeBeltBagInventory(uuid: UUID) {
    beltBagInventories.remove(uuid)
  }

  private fun loadBeltBag(uuid: UUID, type: Material, location: Location): BeltBagInventory {
    try {
      val slotItems = BeltBags.plugin.database.getBeltBagItems(uuid)
      val inventoryHolder = BeltBagInventory(uuid, type)

      for (slotItem in slotItems) {
        if (slotItem.slot >= inventoryHolder.inventory.size) {
          location.world.dropItemNaturally(location.add(BlockFace.UP.direction), slotItem.item)
          continue
        }

        inventoryHolder.inventory.setItem(slotItem.slot, slotItem.item)
      }

      return inventoryHolder
    } catch (e: SQLException) {
      throw RuntimeException(e)
    }
  }
}
