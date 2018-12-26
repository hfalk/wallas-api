package no.falcon.wallasapi.service

import no.falcon.wallasapi.client.AppCenterClient
import no.falcon.wallasapi.domain.appcenter.PushNotificationContent
import org.springframework.stereotype.Component

@Component
class PushNotificationService(private val appCenterClient: AppCenterClient) {
    fun sendStartNotification(temperature: Int) {
        val content = PushNotificationContent(
            "START",
            "Wallas ble startet",
            "Temperatur satt til $temperature grader"
        )

        appCenterClient.postPushNotification(content)
    }

    fun sendChangeNotification(temperature: Int) {
        val content = PushNotificationContent(
            "CHANGE",
            "Wallas ble endret",
            "Temperatur endret til $temperature grader"
        )

        appCenterClient.postPushNotification(content)
    }

    fun sendStopNotification() {
        val content = PushNotificationContent(
            "STOP",
            "Wallas ble stoppet",
            ""
        )

        appCenterClient.postPushNotification(content)
    }
}
