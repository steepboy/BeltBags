package space.subkek.beltbags.util

import org.bukkit.Material
import org.bukkit.SoundCategory
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import space.subkek.beltbags.BeltBagInventory
import space.subkek.beltbags.BeltBags
import java.util.UUID
import java.util.function.Consumer

object BBUtil {
  fun open(uuid: UUID, player: Player, type: Material, beforeOpen: Consumer<BeltBagInventory>?) {
    val inv: BeltBagInventory = BeltBags.plugin.data.getBeltBagInventory(uuid, type, player.location)
    beforeOpen?.accept(inv)
    player.scheduler.runDelayed(BeltBags.plugin, {
      player.openInventory(inv.inv)

      LegacyUtil.getArmorEquipSound(type)
        ?.let { sound -> player.playSound(player, sound, SoundCategory.PLAYERS, 1f, 1f) }
    }, null, 1)
  }

  fun open(uuid: UUID, player: Player, type: Material) {
    open(uuid, player, type, null)
  }

  fun getUUID(item: ItemStack): UUID? {
    val data = item.persistentDataContainer

    val stringUUID = data.get(BeltBags.Keys.BELT_BAG_INV.key, BeltBags.Keys.BELT_BAG_INV.dataType) ?: return null
    return UUID.fromString(stringUUID)
  }
}
