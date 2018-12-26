package no.falcon.wallasapi.service

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import no.falcon.wallasapi.domain.SmsProvider
import no.falcon.wallasapi.properties.FeaturesProperties
import no.falcon.wallasapi.properties.TwillioProperties
import no.falcon.wallasapi.properties.WallasProperties
import org.springframework.stereotype.Service
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sns.model.MessageAttributeValue
import software.amazon.awssdk.services.sns.model.PublishRequest

@Service
class SmsService(
    private val wallasProperties: WallasProperties,
    private val featuresProperties: FeaturesProperties,
    private val twillioProperties: TwillioProperties
) {
    private fun sendSmsAws(smsText: String): String {
        if (featuresProperties.smsProvider == SmsProvider.AWS) {
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

        return "sms-disabled"
    }

    private fun sendSmsTwillio(smsText: String): String {
        if (featuresProperties.smsProvider == SmsProvider.TWILLIO) {
            Twilio.init(twillioProperties.accountSid, twillioProperties.authToken)

            val message = Message
                .creator(
                    PhoneNumber(wallasProperties.phoneNumber),
                    PhoneNumber(twillioProperties.phoneNumber),
                    smsText
                )
                .create()

            return message.sid
        }

        return "sms-disabled"
    }

    fun sendSms(smsText: String): String {
        return when (featuresProperties.smsProvider) {
            SmsProvider.AWS -> sendSmsAws(smsText)
            SmsProvider.TWILLIO -> sendSmsTwillio(smsText)
            SmsProvider.NONE -> "sms_disabled"
        }
    }
}
