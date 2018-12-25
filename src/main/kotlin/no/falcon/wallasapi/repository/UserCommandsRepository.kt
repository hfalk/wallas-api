package no.falcon.wallasapi.repository

import no.falcon.wallasapi.domain.CommandStatus
import no.falcon.wallasapi.domain.CommandType
import no.falcon.wallasapi.domain.UserCommand
import no.falcon.wallasapi.repository.rowmapper.UserCommandRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class UserCommandsRepository(private val jdbcTemplate: JdbcTemplate) {

    fun insertWaitingCommand(type: CommandType, startTime: LocalDateTime, temperature: Int?) {
        jdbcTemplate.update(
            "INSERT INTO public.commands (created_time, type, temperature, start_time, status) VALUES (?, ?, ?, ?, ?)",
            LocalDateTime.now(),
            type.name,
            temperature,
            startTime,
            CommandStatus.WAITING.name
        )
    }

    fun getCommands(): List<UserCommand> {
        return jdbcTemplate.query("SELECT * FROM public.commands", UserCommandRowMapper())
    }

    fun deleteCommand(id: Int) {
        jdbcTemplate.update("DELETE FROM public.commands WHERE id = ?", id)
    }

    fun updateCommandStatus(id: Int, status: CommandStatus) {
        jdbcTemplate.update("UPDATE public.commands SET status = ? WHERE id = ?", status.name, id)
    }

    fun updateCommandMessageId(id: Int, messageId: String) {
        jdbcTemplate.update("UPDATE public.commands SET message_id = ? WHERE id = ?", messageId, id)
    }

    fun getWaitingCommands(): List<UserCommand> {
        return jdbcTemplate.query(
            "SELECT * FROM public.commands WHERE status = ?",
            UserCommandRowMapper(),
            CommandStatus.WAITING.name
        )
    }
}
