package space.subkek.beltbags

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import dev.jorel.commandapi.CommandAPICommand
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.slf4j.Logger
import space.subkek.beltbags.command.IdCommand
import space.subkek.beltbags.command.OpenCommand
import space.subkek.beltbags.data.BBConfig
import space.subkek.beltbags.data.BBData
import space.subkek.beltbags.data.Database
import space.subkek.beltbags.listener.AnvilListener
import space.subkek.beltbags.listener.BreakListener
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
      val key: NamespacedKey = NamespacedKey("etheriaextras", key)
    }

    class Key(key: String) {
      val key: NamespacedKey = NamespacedKey("etheriaextras", key)
    }
  }

  lateinit var config: BBConfig private set
  lateinit var database: Database private set
  val data = BBData()

  private fun registerEvents() {
    server.pluginManager.registerEvents(OpenListener(), this)
    server.pluginManager.registerEvents(CloseListener(), this)
    server.pluginManager.registerEvents(AnvilListener(), this)
    server.pluginManager.registerEvents(BreakListener(), this)
  }

  override fun onLoad() {
    CommandAPI.onLoad(CommandAPIBukkitConfig(this).silentLogs(true).skipReloadDatapacks(true))
  }

  override fun onEnable() {
    CommandAPI.onEnable()

    config = BBConfig.load(this)

    database = Database("data")
    database.createBeltBagTable()

    registerEvents()
    registerCommands()
    registerRecipe()
  }

  override fun onDisable() {
    if (::database.isInitialized) {
      database.close()
    }

    unregisterCommands()
    CommandAPI.onDisable()

    unregisterRecipe()
  }

  private fun registerCommands() {
    CommandAPICommand("beltbags")
      .withAliases("bb")
      .withSubcommand(OpenCommand(this))
      .withSubcommand(IdCommand())
      .executesPlayer(OpenCommand::execute)
      .register("beltbags")
  }

  private fun unregisterCommands() {
    CommandAPI.getRegisteredCommands().forEach { command ->
      CommandAPI.unregister(command.commandName)

      command.aliases.forEach {
        CommandAPI.unregister(it)
      }
    }
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
