package jp.xhw.noticebox.application.external

import java.util.*

interface ItemClaimLogic {

    fun claim(userId: UUID, items: List<ByteArray>)

}