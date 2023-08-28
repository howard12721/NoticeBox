package jp.xhw.noticebox.infrastructure.extensions

import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import java.io.ByteArrayInputStream

@Throws(IllegalStateException::class)
fun ByteArray.toItemStack(): ItemStack {
    return try {
        val inputStream = ByteArrayInputStream(this)
        val input = BukkitObjectInputStream(inputStream)
        val itemStack = input.readObject() as ItemStack
        input.close()
        itemStack
    } catch (e: Exception) {
        throw IllegalStateException("デシリアライズに失敗しました")
    }
}