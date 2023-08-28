package jp.xhw.noticebox.domain.model

class Money(val amount: Double) {
    init {
        require(amount >= 0)
    }
}