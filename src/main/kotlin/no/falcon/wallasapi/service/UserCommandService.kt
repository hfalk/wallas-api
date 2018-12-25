package no.falcon.wallasapi.service

import no.falcon.wallasapi.domain.CommandType
import no.falcon.wallasapi.domain.UserCommand
import no.falcon.wallasapi.properties.TwillioProperties
import org.springframework.stereotype.Service

@Service
class UserCommandService(private val twillioProperties: TwillioProperties) {
    private fun sendStartCommand(temperature: Int): String {
        return "start"
    }

    private fun sendChangeCommand(temperature: Int): String {
        return "change"
    }

    private fun sendStopCommand(): String {
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
