package space.subkek.beltbags.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandArguments
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import space.subkek.beltbags.BBLanguage
import space.subkek.beltbags.BeltBags
import java.util.*

class IdCommand : CommandAPICommand("id") {
  init {
    this.executesPlayer(this::execute)
  }

  private fun getBeltBagUuid(itemStack: ItemStack?): UUID? {
    return itemStack?.persistentDataContainer
      ?.get(BeltBags.Keys.BELT_BAG_INV.key, BeltBags.Keys.BELT_BAG_INV.dataType)
      ?.let(UUID::fromString)
  }

  private fun hasBeltBag(itemStack: ItemStack?): Boolean {
    return getBeltBagUuid(itemStack) != null
  }

  @Suppress("UNUSED_PARAMETER")
  private fun execute(player: Player, arguments: CommandArguments) {
    val leggings = listOf(
      player.inventory.itemInMainHand,
      player.inventory.leggings
    ).firstOrNull { hasBeltBag(it) } ?: run {
      player.sendMessage(BBLanguage.NO_HAND_OR_EQUIP_BELT_BAG.pComponent())
      return
    }

    player.sendMessage(BBLanguage.BELT_BAG_ID.pComponent(getBeltBagUuid(leggings)))
  }
}
