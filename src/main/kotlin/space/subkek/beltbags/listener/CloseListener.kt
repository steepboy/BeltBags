package space.subkek.beltbags.listener

import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.block.ShulkerBox
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BlockStateMeta
import org.bukkit.inventory.meta.BundleMeta
import space.subkek.beltbags.BeltBagInventory
import space.subkek.beltbags.BeltBags
import java.util.function.Predicate

class CloseListener : Listener {
  private fun naturallyDropItem(item: ItemStack, location: Location) {
    location.world.dropItemNaturally(location.add(BlockFace.UP.direction.multiply(0.5)), item)
  }

  private fun isBundle(item: ItemStack): Boolean {
    return item.type.name.contains("BUNDLE")
  }

  private fun isShulker(item: ItemStack): Boolean {
    return item.type.name.contains("SHULKER_BOX")
  }

  private fun dropItemsRecursivelyFromBundle(bundle: ItemStack, location: Location, filter: Predicate<ItemStack>) {
    val bundleMeta = bundle.itemMeta as BundleMeta

    val items = mutableListOf<ItemStack>()
    for (item in bundleMeta.items) {
      if (isBundle(item)) {
        dropItemsRecursivelyFromBundle(item, location, filter)
        items.add(item)

      } else if (filter.test(item)) {
        naturallyDropItem(item, location)

      } else {
        items.add(item)
      }
    }

    bundleMeta.setItems(items)
    bundle.itemMeta = bundleMeta
  }

  private fun dropItemsRecursivelyFromInventory(inventory: Inventory, location: Location, filter: Predicate<ItemStack>) {
    for ((i, item) in inventory.contents.withIndex()) {
      if (item == null) continue

      if (isBundle(item)) {
        dropItemsRecursivelyFromBundle(item, location, filter)

      } else if (isShulker(item)) {
        val meta = item.itemMeta as BlockStateMeta
        val shulker = meta.blockState as ShulkerBox
        val shulkerInv = shulker.inventory

        dropItemsRecursivelyFromInventory(shulkerInv, location, filter)

        meta.blockState = shulker
        item.itemMeta = meta

      } else if (filter.test(item)) {
        naturallyDropItem(item, location)
        inventory.setItem(i, null)
      }
    }
  }

  @EventHandler
  private fun on(event: InventoryCloseEvent) {
    val player = event.player
    if (player !is Player) return
    val beltBagInv = event.inventory.holder
    if (beltBagInv !is BeltBagInventory) return

    dropItemsRecursivelyFromInventory(beltBagInv.inv, player.location) {
      it.persistentDataContainer.has(BeltBags.Keys.BELT_BAG_INV.key)
    }

    beltBagInv.save()
    if (beltBagInv.adminCreated) {
      BeltBags.plugin.data.removeBeltBagInventory(beltBagInv.uuid)
    }
  }
}
