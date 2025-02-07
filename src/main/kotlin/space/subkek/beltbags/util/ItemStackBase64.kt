package space.subkek.beltbags.util

import org.bukkit.inventory.ItemStack
import java.util.*

object ItemStackBase64 {
  fun encode(vararg items: ItemStack): String {
    val itemsBytes = ItemStack.serializeItemsAsBytes(items)

    return Base64.getEncoder().encodeToString(itemsBytes)
  }

  fun decode(itemsBase64: String): Array<ItemStack> {
    val itemsBytes = Base64.getDecoder().decode(itemsBase64)

    return ItemStack.deserializeItemsFromBytes(itemsBytes)
  }
}
