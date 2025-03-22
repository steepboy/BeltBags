package space.subkek.beltbags.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.UUIDArgument
import dev.jorel.commandapi.executors.CommandArguments
import org.bukkit.Material
import org.bukkit.entity.Player
import space.subkek.beltbags.BBLanguage
import space.subkek.beltbags.BeltBags
import java.util.*

class OpenCommand(plugin: BeltBags) : CommandAPICommand("open") {
  init {
    this.withOptionalArguments(
      UUIDArgument("uuid").withPermission("beltbags.admin").replaceSuggestions(
        ArgumentSuggestions.stringCollection {
          plugin.database.getAllBeltBagIds().map { it.toString() }
        }
      )
    )
    this.executesPlayer(OpenCommand::execute)
  }

  companion object {
    fun execute(player: Player, arguments: CommandArguments) {
      var uuid: UUID? = arguments.getByClass("uuid", UUID::class.java)

      val type: Material
      var adminCreated = false
      if (uuid == null) {
        val leggings = player.inventory.leggings ?: run {
          player.sendMessage(BBLanguage.NO_LEGGINGS.pComponent())
          return
        }
        type = leggings.type

        val data = leggings.persistentDataContainer
        val stringUUID = data.get(BeltBags.Keys.BELT_BAG_INV.key, BeltBags.Keys.BELT_BAG_INV.dataType) ?: run {
          player.sendMessage(BBLanguage.NO_EQUIP_BELT_BAG.pComponent())
          return
        }
        uuid = UUID.fromString(stringUUID)
      } else {
        if (!BeltBags.plugin.database.getAllBeltBagIds().contains(uuid)) {
          player.sendMessage(BBLanguage.BELT_BAG_NOT_EXISTS.pComponent())
          return
        }
        type = Material.NETHERITE_LEGGINGS
        adminCreated = true
      }

      val inv = BeltBags.plugin.data.getBeltBagInventory(uuid!!, type, player.location)
      inv.adminCreated = adminCreated
      player.scheduler.runDelayed(BeltBags.plugin, { player.openInventory(inv.inv) }, null, 1)
    }
  }
}
