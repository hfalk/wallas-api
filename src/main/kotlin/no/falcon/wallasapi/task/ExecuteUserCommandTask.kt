package no.falcon.wallasapi.task

import no.falcon.wallasapi.service.UserCommandService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ExecuteUserCommandTask(private val userCommandService: UserCommandService) {
    @Scheduled(fixedDelay = 10000)
    fun executeUserCommand() {
        userCommandService.executeReadyUserCommands()
    }
}
