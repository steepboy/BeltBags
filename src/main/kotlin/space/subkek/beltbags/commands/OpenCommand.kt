package space.subkek.beltbags.commands
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import space.subkek.beltbags.BeltBagInventory
import space.subkek.beltbags.BeltBags
import java.util.*

class OpenCommand {
    fun execute(player: Player) {
        val leggings = player.inventory.leggings ?: return
        if (leggings.type != Material.NETHERITE_LEGGINGS) return

        val meta = leggings.itemMeta ?: return
        val data: PersistentDataContainer = meta.persistentDataContainer

        val stringUUID = data.get(BeltBags.Keys.BELT_BAG_INV.key, BeltBags.Keys.BELT_BAG_INV.dataType) ?: return
        val uuid = UUID.fromString(stringUUID)

        val inv: BeltBagInventory = BeltBags.plugin.data.getBeltBagInventory(uuid)
        player.scheduler.runDelayed(BeltBags.plugin, { player.openInventory(inv.inv) }, null, 1)
    }
}
