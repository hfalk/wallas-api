package no.falcon.wallasapi.service

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import no.falcon.wallasapi.domain.CommandType
import no.falcon.wallasapi.domain.UserCommand
import no.falcon.wallasapi.properties.TwillioProperties
import no.falcon.wallasapi.properties.WallasProperties
import org.springframework.stereotype.Service

@Service
class UserCommandService(
    private val twillioProperties: TwillioProperties,
    private val wallasProperties: WallasProperties
) {
    private fun sendSMS(smsText: String): String {
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

        return sendSMS(smsText)
    }

    private fun sendChangeCommand(temperature: Int): String {
        val smsText = "${wallasProperties.pinCode} W2$temperature"

        return sendSMS(smsText)
    }

    private fun sendStopCommand(): String {
        val smsText = "${wallasProperties.pinCode} W0"

        return sendSMS(smsText)
    }

    fun sendCommand(userCommand: UserCommand): String {
        return when (userCommand.type) {
            CommandType.START -> sendStartCommand(userCommand.temperature)
            CommandType.CHANGE -> sendChangeCommand(userCommand.temperature)
            CommandType.STOP -> sendStopCommand()
        }
    }
}
