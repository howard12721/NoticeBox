package jp.xhw.noticebox.application.exception

import java.util.*

class UserNotFoundException(val userId: UUID) : RuntimeException(userId.toString())