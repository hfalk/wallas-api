package no.falcon.wallasapi.service

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import no.falcon.wallasapi.domain.CommandType
import no.falcon.wallasapi.domain.UserCommand
import no.falcon.wallasapi.properties.TwillioProperties
import no.falcon.wallasapi.properties.WallasProperties
import org.springframework.stereotype.Service
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sns.model.MessageAttributeValue
import software.amazon.awssdk.services.sns.model.PublishRequest

@Service
class UserCommandService(
    private val twillioProperties: TwillioProperties,
    private val wallasProperties: WallasProperties
) {
    private fun sendSMSAws(smsText: String): String {
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

    private fun sendSMSTwillio(smsText: String): String {
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

    private fun sendStartCommand(temperature: Int): String {
        val smsText = "${wallasProperties.pinCode} W1 W2$temperature"

        return sendSMSAws(smsText)
    }

    private fun sendChangeCommand(temperature: Int): String {
        val smsText = "${wallasProperties.pinCode} W2$temperature"

        return sendSMSAws(smsText)
    }

    private fun sendStopCommand(): String {
        val smsText = "${wallasProperties.pinCode} W0"

        return sendSMSAws(smsText)
    }

    fun sendCommand(userCommand: UserCommand): String {
        return when (userCommand.type) {
            CommandType.START -> sendStartCommand(userCommand.temperature)
            CommandType.CHANGE -> sendChangeCommand(userCommand.temperature)
            CommandType.STOP -> sendStopCommand()
        }
    }
}
