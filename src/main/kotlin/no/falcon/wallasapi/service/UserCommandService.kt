package no.falcon.wallasapi.service

import no.falcon.wallasapi.domain.CommandStatus
import no.falcon.wallasapi.domain.CommandType
import no.falcon.wallasapi.domain.UserCommand
import no.falcon.wallasapi.repository.UserCommandsRepository
import no.falcon.wallasapi.util.DateTimeUtil
import org.springframework.stereotype.Service
import java.lang.Exception

@Service
class UserCommandService(
    private val userCommandsRepository: UserCommandsRepository,
    private val smsService: SmsService,
    private val pushNotificationService: PushNotificationService
) {
    private fun sendStartCommand(commandId: Int, temperature: Int) {
        val messageId = smsService.sendStartSms(temperature)
        val pushNotificationId = pushNotificationService.sendStartNotification(temperature)

        userCommandsRepository.updateMessageId(commandId, messageId)
        userCommandsRepository.updatePushNotificationId(commandId, pushNotificationId)
    }

    private fun sendChangeCommand(commandId: Int, temperature: Int) {
        val messageId = smsService.sendChangeSms(temperature)
        val pushNotificationId = pushNotificationService.sendChangeNotification(temperature)

        userCommandsRepository.updateMessageId(commandId, messageId)
        userCommandsRepository.updatePushNotificationId(commandId, pushNotificationId)
    }

    private fun sendStopCommand(commandId: Int) {
        val messageId = smsService.sendStopSms()
        val pushNotificationId = pushNotificationService.sendStopNotification()

        userCommandsRepository.updateMessageId(commandId, messageId)
        userCommandsRepository.updatePushNotificationId(commandId, pushNotificationId)
    }

    private fun sendStatusCommand(commandId: Int) {
        val messageId = smsService.sendStatusSms()

        userCommandsRepository.updateMessageId(commandId, messageId)
    }

    private fun sendCommand(userCommand: UserCommand) {
        try {
            userCommandsRepository.updateCommandStatus(userCommand.id, CommandStatus.IN_PROGRESS)

            when (userCommand.type) {
                CommandType.START -> sendStartCommand(userCommand.id, userCommand.temperature)
                CommandType.CHANGE -> sendChangeCommand(userCommand.id, userCommand.temperature)
                CommandType.STOP -> sendStopCommand(userCommand.id)
                CommandType.STATUS -> sendStatusCommand(userCommand.id)
            }

            userCommandsRepository.updateCommandStatus(userCommand.id, CommandStatus.FINISHED)
        } catch (ex: Exception) {
            userCommandsRepository.updateCommandStatus(userCommand.id, CommandStatus.FAILED)
            throw ex
        }
    }

    fun executeReadyUserCommands() {
        val waitingCommands = userCommandsRepository.getWaitingCommands()

        waitingCommands.forEach {
            if (it.startTime.isBefore(DateTimeUtil.now())) {
                sendCommand(it)
            }
        }
    }
}
