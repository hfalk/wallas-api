package no.falcon.wallasapi.resource

import no.falcon.wallasapi.domain.CommandRequest
import no.falcon.wallasapi.domain.CommandType
import no.falcon.wallasapi.domain.UserCommand
import no.falcon.wallasapi.repository.UserCommandsRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("commands")
class CommandsResource(private val userCommandsRepository: UserCommandsRepository) {
    @GetMapping
    fun getAllUserCommands(): List<UserCommand> {
        return userCommandsRepository.getCommands()
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUserCommand(@PathVariable id: Int) {
        userCommandsRepository.deleteCommand(id)
    }

    @PutMapping("{type}")
    @ResponseStatus(HttpStatus.CREATED)
    fun postCommand(@PathVariable type: CommandType, @RequestBody commandRequest: CommandRequest) {
        userCommandsRepository.insertWaitingCommand(
            type,
            commandRequest.startTime ?: LocalDateTime.now(),
            commandRequest.temperature
        )
    }
}
