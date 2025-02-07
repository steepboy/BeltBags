package space.subkek.beltbags.data

import org.bukkit.Material
import space.subkek.beltbags.BeltBags
import space.subkek.sklib.config.Config
import space.subkek.sklib.config.ConfigField
import space.subkek.sklib.config.provider.ConfigProvider
import space.subkek.sklib.config.provider.toml.TomlConfig
import java.io.File

@Config
class BBConfig {
  @Config
  class TextureConfig {
    @ConfigField
    val beltBagCustomModelData: Int = 300

    @ConfigField
    val leggingsCustomModelData: Int = 300
  }

  @ConfigField
  val beltBagItemMaterial: Material = Material.PAPER

  @ConfigField
  val texture: TextureConfig = TextureConfig()

  companion object {
    fun load(plugin: BeltBags): BBConfig {
      val pluginDirectory = plugin.dataFolder

      val configFile = File(pluginDirectory, "config.toml")

      return toml.load<BBConfig>(BBConfig::class.java, configFile, false)
        .also { toml.save(BBConfig::class.java, it, configFile) }
    }

    private val toml = ConfigProvider.getProvider<ConfigProvider>(
      TomlConfig::class.java
    )
  }
}
