package space.subkek.beltbags

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.slf4j.Logger
import space.subkek.beltbags.data.BBConfig
import space.subkek.beltbags.data.BBData
import space.subkek.beltbags.data.Database
import space.subkek.beltbags.listener.AnvilListener
import space.subkek.beltbags.listener.CloseListener
import space.subkek.beltbags.listener.OpenListener

class BeltBags : JavaPlugin() {
  companion object {
    val plugin: BeltBags get() = getPlugin(BeltBags::class.java)
    val logger: Logger get() = plugin.slF4JLogger
  }

  object Keys {
    val BELT_BAG_ITEM: TypedKey<Byte, Boolean> = TypedKey("beltbag_item", PersistentDataType.BOOLEAN)
    val BELT_BAG_INV: TypedKey<String, String> = TypedKey("beltbag_inv", PersistentDataType.STRING)
    val BELT_BAG_RECIPE: Key = Key("beltbag_recipe")

    class TypedKey<P, C>(key: String, val dataType: PersistentDataType<P, C>) {
      val key: NamespacedKey = NamespacedKey(plugin, key)
    }

    class Key(key: String) {
      val key: NamespacedKey = NamespacedKey(plugin, key)
    }
  }

  val database = Database("data");
  val data = BBData();
  lateinit var config: BBConfig
    private set

  private fun registerEvents() {
    server.pluginManager.registerEvents(OpenListener(), this)
    server.pluginManager.registerEvents(CloseListener(), this)
    server.pluginManager.registerEvents(AnvilListener(), this)
  }

  override fun onEnable() {
    database.createBeltBagTable()
    config = BBConfig.load(this)

    registerEvents()
    registerRecipe()
  }

  override fun onDisable() {
    unregisterRecipe()
  }

  private fun registerRecipe() {
    val beltBagItem = ItemStack(config.beltBagItemMaterial)
    val beltBagItemMeta = beltBagItem.itemMeta

    beltBagItemMeta.setCustomModelData(config.texture.beltBagCustomModelData)
    beltBagItemMeta.itemName(BBLanguage.BELT_BAG_ITEM_NAME.component())
    beltBagItemMeta.persistentDataContainer.set(Keys.BELT_BAG_ITEM.key, Keys.BELT_BAG_ITEM.dataType, true)
    beltBagItem.setItemMeta(beltBagItemMeta)

    val beltBagItemRecipe = ShapedRecipe(Keys.BELT_BAG_RECIPE.key, beltBagItem)
    beltBagItemRecipe.shape(
      "LIL",
      "BLB",
      " I "
    )
    beltBagItemRecipe.setIngredient('L', Material.LEAD)
    beltBagItemRecipe.setIngredient('B', Material.BUNDLE)
    beltBagItemRecipe.setIngredient('I', Material.GOLD_INGOT)

    server.addRecipe(beltBagItemRecipe)
  }

  private fun unregisterRecipe() {
    server.removeRecipe(Keys.BELT_BAG_RECIPE.key)
  }
}
