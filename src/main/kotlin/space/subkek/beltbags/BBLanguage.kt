package space.subkek.beltbags

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

enum class BBLanguage(private val content: String) {
  // Yes, it is really stupid way to make language.
  // #TODO Make multi-language support without changing something else
  PREFIX("<red>BeltBags » <gold>"),
  NO_PERMISSION("<dark_red>У вас нет прав на использование команды."),
  NO_LEGGINGS("<dark_red>На вас нет штанов!"),
  NO_EQUIP_BELT_BAG("<dark_red>На ваших штанах нет поясной сумки!"),
  INVENTORY_NAME("Поясная сумка"),
  LEGGINGS_LORE("<italic:false><gold>▷<gray> Поясная сумка</reset>"),
  BELT_BAG_ITEM_NAME("<light_purple>Поясная сумка"),
  NO_HAND_OR_EQUIP_BELT_BAG("<dark_red>Вы не держите или не одели штаны с поясной сумкой!"),
  BELT_BAG_ID("ID поясной сумки: <red><hover:show_text:'<gold>{0}<newline><gray>Клик чтобы скопировать'><click:copy_to_clipboard:{0}>наведитесь"),
  BELT_BAG_NOT_EXISTS("<dark_red>Такой сумки не существует!");

  private fun string(): String {
    return this.content
  }

  private fun formattedString(vararg replace: Any?): String {
    return formatString(string(), *replace)
  }

  private fun plainString(vararg replace: Any?): String {
    return PLAIN_TEXT.serialize(component(*replace))
  }

  fun component(vararg replace: Any?): Component {
    return MINI_MESSAGE.deserialize(formattedString(*replace))
  }

  fun pComponent(vararg replace: Any?): Component {
    return MINI_MESSAGE.deserialize(PREFIX.string() + formattedString(*replace))
  }

  companion object {
    private val MINI_MESSAGE = MiniMessage.miniMessage()
    private val PLAIN_TEXT = PlainTextComponentSerializer.plainText()

    fun formatString(str: String, vararg replace: Any?): String {
      var result = str
      for (i in replace.indices) {
        result = result.replace("{$i}", replace[i].toString())
      }
      return result
    }

    fun deserialize(content: String, vararg replace: Any?): Component {
      return MINI_MESSAGE.deserialize(formatString(content, *replace))
    }
  }
}
