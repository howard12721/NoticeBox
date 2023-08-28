package jp.xhw.noticebox.presenter.shared

import jp.xhw.noticebox.NoticeBoxPlugin
import org.bukkit.NamespacedKey
import java.time.format.DateTimeFormatter

class Constants {
    companion object {
        val BOOK_KEY = NamespacedKey(NoticeBoxPlugin.plugin, "book_key")
        val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        const val MESSAGE_SEPARATOR = "§a■■■■■■■■■■■■■■■■■■■■■■■■■■■§r"
        const val PAGE_SEPARATOR = "%PAGE_SEPARATOR%"
        const val PREFIX = "§a[NoticeBox]§r "
    }
}