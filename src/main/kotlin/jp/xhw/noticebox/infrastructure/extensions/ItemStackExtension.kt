package jp.xhw.noticebox.infrastructure.extensions

import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayOutputStream

@Throws(IllegalStateException::class)
fun ItemStack.toByteArray(): ByteArray {
    return try {
        val outputStream = ByteArrayOutputStream()
        val output = BukkitObjectOutputStream(outputStream)
        output.writeObject(this)
        output.close()
        outputStream.toByteArray()
    } catch (e: Exception) {
        throw IllegalStateException("シリアライズに失敗しました")
    }
}