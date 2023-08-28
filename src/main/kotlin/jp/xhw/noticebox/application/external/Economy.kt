package jp.xhw.noticebox.application.external

import java.util.*

interface Economy {
    fun giveMoney(userId: UUID, amount: Double)

}