package no.falcon.wallasapi.client

import no.falcon.wallasapi.domain.appcenter.PushNotificationContent
import no.falcon.wallasapi.domain.appcenter.PushNotificationRequest
import no.falcon.wallasapi.domain.appcenter.PushNotificationResponse
import no.falcon.wallasapi.properties.AppCenterProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.client.postForObject

@Service
class AppCenterClient(
    private val restTemplateBuilder: RestTemplateBuilder,
    private val appCenterProperties: AppCenterProperties
) {
    fun postPushNotification(content: PushNotificationContent): PushNotificationResponse? {
        val restTemplate = restTemplateBuilder.rootUri(appCenterProperties.baseUrl).build()

        val request = PushNotificationRequest(content)

        val headers = HttpHeaders()
        headers.add("X-API-Token", appCenterProperties.apiToken)

        val httpEntity = HttpEntity(request, headers)

        return restTemplate.postForObject(
            "${appCenterProperties.baseUrl}/${appCenterProperties.ownerName}/${appCenterProperties.androidAppName}/push/notifications",
            httpEntity
        )
    }
}
