package no.falcon.wallasapi.resource

import no.falcon.wallasapi.domain.CommandRequest
import no.falcon.wallasapi.domain.CommandStatus
import no.falcon.wallasapi.domain.CommandType
import no.falcon.wallasapi.domain.UserCommand
import no.falcon.wallasapi.repository.UserCommandsRepository
import no.falcon.wallasapi.util.DateTimeUtil
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("commands")
class CommandsResource(private val userCommandsRepository: UserCommandsRepository) {
    @GetMapping
    fun getAllUserCommands(@RequestParam(value = "status", required = false) status: CommandStatus?): List<UserCommand> {
        if (status != null) {
            return userCommandsRepository.getCommands().filter { it.status == status }
        }

        return userCommandsRepository.getCommands()
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUserCommand(@PathVariable id: Int) {
        userCommandsRepository.deleteCommand(id)
    }

    @PutMapping("{type}")
    @ResponseStatus(HttpStatus.CREATED)
    fun putCommand(@PathVariable type: CommandType, @RequestBody commandRequest: CommandRequest) {
        userCommandsRepository.insertWaitingCommand(
            type,
            commandRequest.startTime ?: DateTimeUtil.now(),
            commandRequest.temperature
        )
    }
}
