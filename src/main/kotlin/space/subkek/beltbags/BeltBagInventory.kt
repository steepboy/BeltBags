package space.subkek.beltbags

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import java.sql.SQLException
import java.util.*

class BeltBagInventory(val uuid: UUID, player: Player) : InventoryHolder {
  private enum class InventorySize(private val component: Int) {
    SMALL(9),
    MEDIUM(18),
    LARGE(27);

    fun getSize(): Int {
      return this.component
    }

  }
  val inv: Inventory = BeltBags.plugin.server.createInventory(
    this, getInventorySize(player), BBLanguage.INVENTORY_NAME.component()
  )

  override fun getInventory(): Inventory {
    return inv
  }

  private fun getInventorySize(player: Player): Int{
    val leggings = player.inventory.leggings?.type
    return when (leggings) {
        Material.NETHERITE_LEGGINGS -> InventorySize.LARGE
        in setOf(Material.DIAMOND_LEGGINGS, Material.GOLDEN_LEGGINGS) -> InventorySize.MEDIUM
        else -> InventorySize.SMALL
    }.getSize()
  }

  fun save() {
    try {
      BeltBags.plugin.database.saveBeltBagInventory(this)
    } catch (e: SQLException) {
      BeltBags.logger.error("Failed to save beltbag: ", e)
    }
  }
}
