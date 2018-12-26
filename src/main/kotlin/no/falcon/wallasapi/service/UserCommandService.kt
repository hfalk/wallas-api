package no.falcon.wallasapi.service

import no.falcon.wallasapi.domain.CommandStatus
import no.falcon.wallasapi.domain.CommandType
import no.falcon.wallasapi.domain.UserCommand
import no.falcon.wallasapi.properties.WallasProperties
import no.falcon.wallasapi.repository.UserCommandsRepository
import no.falcon.wallasapi.util.DateTimeUtil
import org.springframework.stereotype.Service
import java.lang.Exception

@Service
class UserCommandService(
    private val wallasProperties: WallasProperties,
    private val userCommandsRepository: UserCommandsRepository,
    private val smsService: SmsService,
    private val pushNotificationService: PushNotificationService
) {
    private fun sendStartCommand(temperature: Int): String {
        val smsText = "${wallasProperties.pinCode} W1 W2$temperature"

        val messageId = smsService.sendSms(smsText)

        pushNotificationService.sendStartNotification(temperature)

        return messageId
    }

    private fun sendChangeCommand(temperature: Int): String {
        val smsText = "${wallasProperties.pinCode} W2$temperature"

        val messageId = smsService.sendSms(smsText)

        pushNotificationService.sendChangeNotification(temperature)

        return messageId
    }

    private fun sendStopCommand(): String {
        val smsText = "${wallasProperties.pinCode} W0"

        val messageId = smsService.sendSms(smsText)

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
