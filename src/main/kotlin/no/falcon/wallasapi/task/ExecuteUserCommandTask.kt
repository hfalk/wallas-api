package no.falcon.wallasapi.task

import no.falcon.wallasapi.domain.CommandStatus
import no.falcon.wallasapi.repository.UserCommandsRepository
import no.falcon.wallasapi.service.UserCommandService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.lang.Exception

@Component
class ExecuteUserCommandTask(
    private val userCommandsRepository: UserCommandsRepository,
    private val userCommandService: UserCommandService
) {
    @Scheduled(fixedDelay = 30000)
    fun executeUserCommand() {
        val waitingCommands = userCommandsRepository.getWaitingCommands()

        waitingCommands.forEach {
            try {
                userCommandsRepository.updateCommandStatus(it.id, CommandStatus.IN_PROGRESS)

                val messageId = userCommandService.sendCommand(it)

                userCommandsRepository.updateCommandStatus(it.id, CommandStatus.FINISHED)
                userCommandsRepository.updateCommandMessageId(it.id, messageId)
            } catch (ex: Exception) {
                userCommandsRepository.updateCommandStatus(it.id, CommandStatus.FAILED)
            }
        }
    }
}
