package jp.xhw.noticebox.application.service

import jp.xhw.noticebox.application.dto.RewardDto
import jp.xhw.noticebox.application.external.Economy
import jp.xhw.noticebox.application.external.ItemClaimLogic
import jp.xhw.noticebox.domain.model.AnnounceId
import jp.xhw.noticebox.domain.model.UserId
import jp.xhw.noticebox.domain.repository.AnnounceRepository
import jp.xhw.noticebox.domain.repository.UserRepository
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class UserOpenAnnounceService(
    private val userRepository: UserRepository,
    private val announceRepository: AnnounceRepository,
    private val economy: Economy,
    private val itemClaimLogic: ItemClaimLogic
) {
    fun open(userId: UUID, announceId: UUID): RewardDto? {
        return transaction {
            val opener = userRepository.find(UserId(userId))
            val announce = announceRepository.findById(AnnounceId(announceId)) ?: return@transaction null

            if (opener.isNotOpened(announce)) {
                opener.openAnnounce(announce)
                economy.giveMoney(opener.userId.value, announce.reward.money.amount)
                itemClaimLogic.claim(opener.userId.value, announce.reward.items)

                userRepository.save(opener)

                return@transaction RewardDto(announce.reward.money.amount, announce.reward.items)
            }

            return@transaction null
        }
    }
}