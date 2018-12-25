package no.falcon.wallasapi.service

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
    private fun sendStartCommand(temperature: Int): String {
        val smsString = "${wallasProperties.pinCode} W1 W2$temperature"

        return "start"
    }

    private fun sendChangeCommand(temperature: Int): String {
        val smsString = "${wallasProperties.pinCode} W2$temperature"

        return "change"
    }

    private fun sendStopCommand(): String {
        val smsString = "${wallasProperties.pinCode} W0"

        return "stop"
    }

    fun sendCommand(userCommand: UserCommand): String {
        return when (userCommand.type) {
            CommandType.START -> sendStartCommand(userCommand.temperature)
            CommandType.CHANGE -> sendChangeCommand(userCommand.temperature)
            CommandType.STOP -> sendStopCommand()
        }
    }
}
