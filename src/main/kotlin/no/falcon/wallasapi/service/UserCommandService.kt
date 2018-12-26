package no.falcon.wallasapi.service

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import no.falcon.wallasapi.domain.CommandStatus
import no.falcon.wallasapi.domain.CommandType
import no.falcon.wallasapi.domain.SmsProvider
import no.falcon.wallasapi.domain.UserCommand
import no.falcon.wallasapi.properties.FeaturesProperties
import no.falcon.wallasapi.properties.TwillioProperties
import no.falcon.wallasapi.properties.WallasProperties
import no.falcon.wallasapi.repository.UserCommandsRepository
import no.falcon.wallasapi.util.DateTimeUtil
import org.springframework.stereotype.Service
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sns.model.MessageAttributeValue
import software.amazon.awssdk.services.sns.model.PublishRequest
import java.lang.Exception

@Service
class UserCommandService(
    private val twillioProperties: TwillioProperties,
    private val wallasProperties: WallasProperties,
    private val userCommandsRepository: UserCommandsRepository,
    private val featuresProperties: FeaturesProperties,
    private val pushNotificationService: PushNotificationService
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

    private fun sendSms(smsText: String): String {
        return when (featuresProperties.smsProvider) {
            SmsProvider.AWS -> sendSmsAws(smsText)
            SmsProvider.TWILLIO -> sendSmsTwillio(smsText)
            SmsProvider.NONE -> "sms_disabled"
        }
    }

    private fun sendStartCommand(temperature: Int): String {
        val smsText = "${wallasProperties.pinCode} W1 W2$temperature"

        val messageId = sendSms(smsText)

        pushNotificationService.sendStartNotification(temperature)

        return messageId
    }

    private fun sendChangeCommand(temperature: Int): String {
        val smsText = "${wallasProperties.pinCode} W2$temperature"

        val messageId = sendSms(smsText)

        pushNotificationService.sendChangeNotification(temperature)

        return messageId
    }

    private fun sendStopCommand(): String {
        val smsText = "${wallasProperties.pinCode} W0"

        val messageId = sendSms(smsText)

        pushNotificationService.sendStopNotification()

        return messageId
    }

    private fun sendCommand(userCommand: UserCommand): String {
        return when (userCommand.type) {
            CommandType.START -> sendStartCommand(userCommand.temperature)
            CommandType.CHANGE -> sendChangeCommand(userCommand.temperature)
            CommandType.STOP -> sendStopCommand()
        }
    }

    fun executeReadyUserCommands() {
        val waitingCommands = userCommandsRepository.getWaitingCommands()

        waitingCommands.forEach {
            if (it.startTime.isBefore(DateTimeUtil.now())) {
                try {
                    userCommandsRepository.updateCommandStatus(it.id, CommandStatus.IN_PROGRESS)

                    val messageId = sendCommand(it)

                    userCommandsRepository.updateCommandStatus(it.id, CommandStatus.FINISHED)
                    userCommandsRepository.updateMessageId(it.id, messageId)
                } catch (ex: Exception) {
                    userCommandsRepository.updateCommandStatus(it.id, CommandStatus.FAILED)
                    throw ex
                }
            }
        }
    }
}
