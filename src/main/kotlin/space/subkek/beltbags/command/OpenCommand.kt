package space.subkek.beltbags.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandArguments
import org.bukkit.Material
import org.bukkit.entity.Player
import space.subkek.beltbags.BBLanguage
import space.subkek.beltbags.BeltBags
import java.util.*


class OpenCommand : CommandAPICommand("open") {
  init {
    this.executesPlayer(OpenCommand::execute)
  }

  companion object {
    @Suppress("UNUSED_PARAMETER")
    fun execute(player: Player, arguments: CommandArguments) {
      val leggings = player.inventory.leggings ?: run {
        player.sendMessage(BBLanguage.NO_LEGGINGS.pComponent())
        return
      }

      if (leggings.type != Material.NETHERITE_LEGGINGS) {
        player.sendMessage(BBLanguage.NO_BELT_BAG_LEGGINGS.pComponent())
        return
      }

      val data = leggings.persistentDataContainer
      val stringUUID = data.get(BeltBags.Keys.BELT_BAG_INV.key, BeltBags.Keys.BELT_BAG_INV.dataType) ?: run {
        player.sendMessage(BBLanguage.NO_BELT_BAG_LEGGINGS.pComponent())
        return
      }
      val uuid = UUID.fromString(stringUUID)

      val inv = BeltBags.plugin.data.getBeltBagInventory(uuid)
      player.scheduler.runDelayed(BeltBags.plugin, { player.openInventory(inv.inv) }, null, 1)
    }
  }
}
