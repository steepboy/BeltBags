package space.subkek.beltbags

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import java.sql.SQLException
import java.util.*

class BeltBagInventory(val uuid: UUID) : InventoryHolder {
  val inv: Inventory = BeltBags.plugin.server.createInventory(
    this, 18, BBLanguage.INVENTORY_NAME.component()
  )

  override fun getInventory(): Inventory {
    return inv
  }

  fun save() {
    try {
      BeltBags.plugin.database.saveBeltBagInventory(this)
    } catch (e: SQLException) {
      BeltBags.logger.error("Failed to save beltbag: ", e)
    }
  }
}
