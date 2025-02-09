package space.subkek.beltbags.commands
import dev.jorel.commandapi.executors.CommandArguments
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import java.util.UUID

import space.subkek.beltbags.BBLanguage
import space.subkek.beltbags.BeltBagInventory
import space.subkek.beltbags.BeltBags


class OpenCommand {
    fun execute(player: Player, args: CommandArguments) {
        val leggings = player.inventory.leggings ?: run {
            player.sendMessage(BBLanguage.NO_PANTS.component())
            return
        }
        if (leggings.type != Material.NETHERITE_LEGGINGS) {
            player.sendMessage(BBLanguage.INVALID_PANTS_TYPE.component())
            return
        }

        val meta = leggings.itemMeta ?: return
        val data: PersistentDataContainer = meta.persistentDataContainer

        val stringUUID = data.get(BeltBags.Keys.BELT_BAG_INV.key, BeltBags.Keys.BELT_BAG_INV.dataType) ?: run {
            player.sendMessage(BBLanguage.NO_BAGS_ON_PANTS.component())
            return
        }
        val uuid = UUID.fromString(stringUUID)

        val inv: BeltBagInventory = BeltBags.plugin.data.getBeltBagInventory(uuid)
        player.scheduler.runDelayed(BeltBags.plugin, { player.openInventory(inv.inv) }, null, 1)
    }
}
