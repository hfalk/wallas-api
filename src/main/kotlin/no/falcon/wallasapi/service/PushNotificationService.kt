package no.falcon.wallasapi.service

import no.falcon.wallasapi.client.AppCenterClient
import no.falcon.wallasapi.domain.StatusContent
import no.falcon.wallasapi.domain.appcenter.PushNotificationContent
import org.springframework.stereotype.Component

@Component
class PushNotificationService(private val appCenterClient: AppCenterClient) {
    private fun sendPushNotification(name: String, title: String, body: String): String {
        val content = PushNotificationContent(name, title, body)

        val response = appCenterClient.postPushNotification(content)

        return response?.notification_id ?: "notification_id_unavailable"
    }

    fun sendStartNotification(temperature: Int) =
        sendPushNotification("START", "Wallas ble startet", "Temperatur satt til $temperature grader")

    fun sendChangeNotification(temperature: Int) =
        sendPushNotification("CHANGE", "Wallas ble endret", "Temperatur endret til $temperature grader")

    fun sendStopNotification() = sendPushNotification("STOP", "Wallas ble stoppet", "")

    fun sendStatusNotification(statusContent: StatusContent) = sendPushNotification(
        "STATUS",
        "Wallas status",
        "${statusContent.message} Set temp: ${statusContent.setTemp}. Read temp: ${statusContent.readTemp}. Volt: ${statusContent.volt}"
    )
}
