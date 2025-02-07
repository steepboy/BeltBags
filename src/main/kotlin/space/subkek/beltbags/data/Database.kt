package space.subkek.beltbags.data

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import space.subkek.beltbags.BeltBagInventory
import space.subkek.beltbags.BeltBags
import space.subkek.beltbags.util.ItemStackBase64
import java.nio.file.Path
import java.sql.*
import java.util.*

class Database(private var path: String) : AutoCloseable {
  private val connection: Connection

  init {
    path = Path.of(BeltBags.plugin.dataFolder.absolutePath, path).toString()
    Class.forName("org.h2.Driver")
    connection = DriverManager.getConnection("jdbc:h2:$path")
  }

  @Throws(SQLException::class)
  override fun close() {
    connection.close()
  }

  @Throws(SQLException::class)
  fun createBeltBagTable() {
    connection.createStatement().execute(
      """
        CREATE TABLE IF NOT EXISTS belt_bags (
            id UUID NOT NULL,
            slot INT NOT NULL,
            item TEXT NOT NULL
        )
      """
    )
  }

  data class BeltBagSlotItem(val slot: Int, val item: ItemStack)

  @Throws(SQLException::class)
  fun getBeltBagItems(uuid: UUID): List<BeltBagSlotItem> {
    val statement = connection.prepareStatement("SELECT * FROM belt_bags WHERE id = ?")
    statement.setObject(1, uuid)
    val resultSet = statement.executeQuery()

    val result: MutableList<BeltBagSlotItem> = ArrayList()
    while (resultSet.next()) {
      val slot = resultSet.getInt("slot")
      val itemData = resultSet.getString("item")
      val item: ItemStack = ItemStackBase64.decode(itemData)[0]

      result.add(BeltBagSlotItem(slot, item))
    }

    return result
  }

  @Throws(SQLException::class)
  fun saveBeltBagInventory(inventoryHolder: BeltBagInventory) {
    val uuid: UUID = inventoryHolder.uuid
    val inventory: Inventory = inventoryHolder.inv

    connection.prepareStatement(
      "DELETE FROM belt_bags WHERE id = ?"
    ).use { statement ->
      statement.setObject(1, uuid)
      statement.execute()
    }
    connection.prepareStatement(
      "INSERT INTO belt_bags (id, slot, item) VALUES (?, ?, ?)"
    ).use { statement ->
      statement.setObject(1, uuid)
      for (i in 0 until inventory.size) {
        val item = inventory.getItem(i) ?: continue

        statement.setInt(2, i)
        val encodedItem: String = ItemStackBase64.encode(item)
        statement.setString(3, encodedItem)

        statement.addBatch()
      }
      statement.executeBatch()
    }
  }
}
