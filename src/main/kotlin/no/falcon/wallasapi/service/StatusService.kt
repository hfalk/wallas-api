package no.falcon.wallasapi.service

import no.falcon.wallasapi.repository.StatusRepository
import org.springframework.stereotype.Service

@Service
class StatusService(
    private val pushNotificationService: PushNotificationService,
    private val statusRepository: StatusRepository
) {
    fun insertStatus(statusRawString: String, messageId: String) {
        val statusId = statusRepository.insertStatus(statusRawString, messageId)
        val pushNotificationId = pushNotificationService.sendStatusNotification(statusRawString)

        statusRepository.updatePushNotificationId(statusId, pushNotificationId)
    }

    fun getStatuses() = statusRepository.getStatuses()
}
