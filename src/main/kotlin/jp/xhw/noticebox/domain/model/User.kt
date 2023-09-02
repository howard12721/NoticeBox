package jp.xhw.noticebox.domain.model

import jp.xhw.noticebox.domain.exceptions.AnnounceAlreadyOpenedException
import java.time.LocalDateTime
import java.util.*

class User(val userId: UserId, val openedAnnounceIds: MutableList<AnnounceId>, val firstJoin: LocalDateTime) {

    fun openAnnounce(announce: Announce) {
        if (openedAnnounceIds.contains(announce.announceId)) {
            throw AnnounceAlreadyOpenedException()
        }
        openedAnnounceIds.add(announce.announceId)
    }

    fun isNotOpened(announce: Announce): Boolean {
        return !openedAnnounceIds.contains(announce.announceId)
    }

}

data class UserId(val value: UUID)