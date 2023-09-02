package jp.xhw.noticebox.domain.repository

import jp.xhw.noticebox.domain.model.User
import jp.xhw.noticebox.domain.model.UserId

interface UserRepository {

    fun find(userId: UserId): User?
    fun save(user: User)

}