package space.subkek.beltbags.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandArguments
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import space.subkek.beltbags.BBLanguage
import space.subkek.beltbags.util.BBUtil
import java.util.*

class IdCommand : CommandAPICommand("id") {
  init {
    this.executesPlayer(this::execute)
  }

  private fun getBeltBagUuid(item: ItemStack?): UUID? {
    item ?: return null
    return BBUtil.getUUID(item)
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
