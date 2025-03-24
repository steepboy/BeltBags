package space.subkek.beltbags.util

import org.bukkit.Material
import org.bukkit.Sound

object LegacyUtil {
  fun getArmorEquipSound(type: Material): Sound? {
    val name = type.name.lowercase()

    if (name.contains("chainmail")) {
      return Sound.ITEM_ARMOR_EQUIP_CHAIN
    } else if (name.contains("iron")) {
      return Sound.ITEM_ARMOR_EQUIP_IRON
    } else if (name.contains("golden")) {
      return Sound.ITEM_ARMOR_EQUIP_GOLD
    } else if (name.contains("diamond")) {
      return Sound.ITEM_ARMOR_EQUIP_DIAMOND
    } else if (name.contains("netherite")) {
      return Sound.ITEM_ARMOR_EQUIP_NETHERITE
    }
    
    return null
  }
}
