package jp.xhw.noticebox.presenter.shared

import jp.xhw.noticebox.NoticeBoxPlugin
import org.bukkit.NamespacedKey
import java.time.format.DateTimeFormatter

class Constants {
    companion object {
        val BOOK_KEY = NamespacedKey(NoticeBoxPlugin.plugin, "book_key")
        const val ANNOUNCE_BOOK_TAG_VALUE = "announce" // Value for the BOOK_KEY PDC tag
        val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        const val MESSAGE_SEPARATOR = "§a■■■■■■■■■■■■■■■■■■■■■■■■■■■§r"
        const val PAGE_SEPARATOR = "%PAGE_SEPARATOR%"
        const val PREFIX = "§a[NoticeBox]§r "

        // User-facing strings
        const val TITLE_ANNOUNCE_BOOK_INPUT = "§lお知らせの本文を入力"
        const val LORE_ANNOUNCE_BOOK_SIGN = "§a署名して決定"
        const val MESSAGE_INVENTORY_FULL = "$PREFIX§cインベントリが一杯です"
        const val MENU_TITLE_DELETE = "削除メニュー"
        const val MENU_TITLE_BOX = "お知らせ"
        const val MENU_TITLE_CREATE_ANNOUNCE = "アナウンスをする"

        // GUI Strings
        const val GUI_TEXT_EMPTY = " "
        const val GUI_BUTTON_FIRST_PAGE = "最初のページへ"
        const val GUI_BUTTON_PREVIOUS_PAGE = "前のページへ"
        const val GUI_BUTTON_NEXT_PAGE = "次のページへ"
        const val GUI_MESSAGE_MONEY_REWARD = "お金 §6%d§r"
        const val GUI_MESSAGE_ITEM_REWARD = "アイテム §a%d§r個"
        const val GUI_ERROR_REWARD_USER_NOT_FOUND = "$PREFIX§c報酬の受け取り中にエラーが発生しました: ユーザーが見つかりませんでした"
        const val GUI_ERROR_ANNOUNCE_CONTENT_NOT_FOUND = "エラー: 内容が見つかりませんでした"
        const val GUI_DEFAULT_ANNOUNCE_TITLE = "不明なお知らせ"
        const val GUI_BOOK_AUTHOR = "NoticeBox"
        const val GUI_PAGE_TITLE_FORMAT = "ページ: %d" // Placeholder for current page
        const val GUI_ERROR_LOAD_ANNOUNCES_USER_NOT_FOUND = "$PREFIX§cお知らせの読み込み中にエラーが発生しました: ユーザーが見つかりませんでした"

        // CreateAnnounceMenu Strings
        const val ANVIL_TITLE_ENTER_TITLE = "§lタイトルを入力"
        const val CREATE_ANNOUNCE_BUTTON_SET_TITLE = "§lタイトルを設定"
        const val CREATE_ANNOUNCE_LABEL_CURRENT_PREFIX = "§a現在§f: "
        const val ANVIL_TEXT_ENTER_MONEY = "与える金額を入力"
        const val ANVIL_TEXT_INVALID_NUMBER = "無効な数値です"
        const val CREATE_ANNOUNCE_BUTTON_SET_MONEY = "§l報酬としてお金を与える"
        const val ITEM_STORAGE_MENU_TITLE = "与えるアイテムを入れてください"
        const val CREATE_ANNOUNCE_BUTTON_SET_ITEMS = "§l報酬としてアイテムを与える"
        const val CREATE_ANNOUNCE_LABEL_ITEM_COUNT_SUFFIX = "個のアイテム"
        const val MESSAGE_ANNOUNCE_CREATED = "$PREFIXお知らせを作成しました"
        const val CREATE_ANNOUNCE_BUTTON_CREATE = "§l§aお知らせを作成する"

        // DeleteAnnounceMenu / DeleteConfirmMenu Strings
        const val MENU_TITLE_DELETE_CONFIRM = "削除を確認"
        const val MESSAGE_ANNOUNCE_DELETED_FORMAT = "$PREFIXお知らせ「%s」を削除しました" // Placeholder for title
        const val DELETE_CONFIRM_BUTTON_DELETE = "§l§c削除する"
        const val DELETE_CONFIRM_BUTTON_CANCEL = "§l§cキャンセル"

        // ItemStorageMenu Strings
        const val ITEM_STORAGE_TEMP_INVENTORY_TITLE = "ここにアイテムを入れる"
        const val ITEM_STORAGE_BUTTON_BACK = "§c§l戻る"

        // Command specific messages
        const val MESSAGE_SPECIFY_PLAYER_FOR_CONSOLE = "$PREFIX§cコンソールから実行する場合、対象のプレイヤーを指定してください。"
        const val MESSAGE_NO_PERMISSION_OPEN_OTHER = "$PREFIX§c他のプレイヤーのNoticeBoxを開く権限がありません。" // General no permission
        const val MESSAGE_PLAYER_NOT_FOUND = "$PREFIX§c指定されたプレイヤーが見つかりません。"


        // Permission strings
        const val PERMISSION_ADD = "noticebox.add"
        const val PERMISSION_REMOVE = "noticebox.remove"
        const val PERMISSION_OPEN_OTHER = "noticebox.open-other"
        const val PERMISSION_OPEN = "noticebox.open"
        const val PERMISSION_VIEW_ALL = "noticebox.viewall"
    }
}