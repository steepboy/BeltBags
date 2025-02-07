package space.subkek.beltbags.listener

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareAnvilEvent
import space.subkek.beltbags.BBLanguage
import space.subkek.beltbags.BeltBags
import java.util.*

@Suppress("UnstableApiUsage")
class AnvilListener : Listener {
  @EventHandler
  fun on(event: PrepareAnvilEvent) {
    val anvilView = event.view

    val firstItem = anvilView.getItem(0)
    val secondItem = anvilView.getItem(1)

    if (firstItem == null || secondItem == null) return
    if (firstItem.type != Material.NETHERITE_LEGGINGS) return
    val firstItemData = firstItem.itemMeta.persistentDataContainer
    if (firstItemData.has(BeltBags.Keys.BELT_BAG_INV.key)) return
    if (secondItem.type != BeltBags.plugin.config.beltBagItemMaterial) return
    if (secondItem.amount > 1) return
    if (!secondItem.hasItemMeta()) return
    val secondItemData = secondItem.itemMeta.persistentDataContainer
    if (!secondItemData.has(BeltBags.Keys.BELT_BAG_ITEM.key)) return

    val firstItemClone = firstItem.clone()
    val itemMeta = firstItemClone.itemMeta

    anvilView.renameText?.let {
      if (it.isBlank()) return@let
      itemMeta.customName(Component.text(it))
    }

    var oldLore = itemMeta.lore()
    if (oldLore == null) oldLore = ArrayList()
    oldLore.add(BBLanguage.LEGGINGS_LORE.component())
    itemMeta.lore(oldLore)

    itemMeta.setCustomModelData(BeltBags.plugin.config.texture.leggingsCustomModelData)

    itemMeta.persistentDataContainer.set(
      BeltBags.Keys.BELT_BAG_INV.key, BeltBags.Keys.BELT_BAG_INV.dataType, UUID.randomUUID().toString()
    )
    firstItemClone.setItemMeta(itemMeta)

    anvilView.repairCost = 20

    event.result = firstItemClone
  }
}
