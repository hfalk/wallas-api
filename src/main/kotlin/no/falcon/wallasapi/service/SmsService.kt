package no.falcon.wallasapi.service

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import no.falcon.wallasapi.domain.SmsProvider
import no.falcon.wallasapi.properties.FeaturesProperties
import no.falcon.wallasapi.properties.TwilioProperties
import no.falcon.wallasapi.properties.WallasProperties
import org.springframework.stereotype.Service
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sns.model.MessageAttributeValue
import software.amazon.awssdk.services.sns.model.PublishRequest
import ClickSend.ApiClient
import ClickSend.Model.SmsMessageCollection
import ClickSend.Model.SmsMessage
import ClickSend.Api.SmsApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import no.falcon.wallasapi.domain.ClickSendSmsResponse
import no.falcon.wallasapi.properties.ClickSendProperties


@Service
class SmsService(
    private val wallasProperties: WallasProperties,
    private val featuresProperties: FeaturesProperties,
    private val twilioProperties: TwilioProperties,
    private val clickSendProperties: ClickSendProperties
) {
    private fun sendSmsAws(smsText: String): String {
        val snsClient = SnsClient.builder()
            .region(Region.EU_WEST_1)
            .build()

        val smsAttributes = mutableMapOf<String, MessageAttributeValue>(
            "AWS.SNS.SMS.SenderID" to MessageAttributeValue.builder()
                .dataType("String")
                .stringValue("95184532")
                .build()
        )

        val request = PublishRequest.builder()
            .phoneNumber(wallasProperties.phoneNumber)
            .message(smsText)
            .messageAttributes(smsAttributes)
            .build()

        val response = snsClient.publish(request)

        return response.messageId()
    }

    private fun sendSmsTwilio(smsText: String): String {
        Twilio.init(twilioProperties.accountSid, twilioProperties.authToken)

        val message = Message
            .creator(
                PhoneNumber(wallasProperties.phoneNumber),
                twilioProperties.serviceSid,
                smsText
            )
            .create()

        return message.sid
    }

    private fun sendSmsClickSend(smsText: String): String {
        val defaultClient = ApiClient()
        defaultClient.setUsername(clickSendProperties.userName)
        defaultClient.setPassword(clickSendProperties.apiKey)
        val apiInstance = SmsApi(defaultClient)

        val smsMessage = SmsMessage().apply {
            body = smsText
            to = wallasProperties.phoneNumber
        }
        val smsMessages = SmsMessageCollection().apply {
            messages = listOf(smsMessage)
        }

        val result = apiInstance.smsSendPost(smsMessages)
        val response = Json { ignoreUnknownKeys = true }.decodeFromString(ClickSendSmsResponse.serializer(), result)
        response.data.messages.first().status.let {
            if (it != "SUCCESS") {
                throw Exception("ClickSend exception: $it")
            }
        }
        return response.data.messages.first().message_id
    }

    private fun sendSms(smsText: String): String {
        return when (featuresProperties.smsProvider) {
            SmsProvider.AWS -> sendSmsAws(smsText)
            SmsProvider.TWILIO -> sendSmsTwilio(smsText)
            SmsProvider.CLICK_SEND -> sendSmsClickSend(smsText)
            SmsProvider.NONE -> "sms_disabled"
        }
    }

    fun sendStartSms(temperature: Int): String = sendSms("${wallasProperties.pinCode} W1 W2$temperature")

    fun sendChangeSms(temperature: Int): String = sendSms("${wallasProperties.pinCode} W2$temperature")

    fun sendStopSms(): String = sendSms("${wallasProperties.pinCode} W0")

    fun sendStatusSms(): String = sendSms("${wallasProperties.pinCode} S4")
}
