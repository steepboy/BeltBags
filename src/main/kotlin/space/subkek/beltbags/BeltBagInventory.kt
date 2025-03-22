package space.subkek.beltbags

import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import java.sql.SQLException
import java.util.*

class BeltBagInventory(val uuid: UUID, private val type: Material) : InventoryHolder {
  private enum class InventorySize(val size: Int, val types: Set<Material>) {
    SMALL(9, setOf(Material.IRON_LEGGINGS, Material.CHAINMAIL_LEGGINGS)),
    MEDIUM(18, setOf(Material.DIAMOND_LEGGINGS, Material.GOLDEN_LEGGINGS)),
    LARGE(27, setOf(Material.NETHERITE_LEGGINGS));

    companion object {
      fun getSizeByType(type: Material): Optional<Int> {
        for (sizeClass in InventorySize.entries) {
          if (type in sizeClass.types) {
            return Optional.of(sizeClass.size)
          }
        }
        return Optional.empty()
      }
    }
  }

  companion object {
    fun canCreate(type: Material): Boolean {
      return InventorySize.getSizeByType(type).isPresent
    }
  }

  var adminCreated = false

  val inv: Inventory = BeltBags.plugin.server.createInventory(
    this, InventorySize.getSizeByType(type).orElseThrow(), BBLanguage.INVENTORY_NAME.component()
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
