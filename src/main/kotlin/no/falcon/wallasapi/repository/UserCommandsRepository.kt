package no.falcon.wallasapi.repository

import no.falcon.wallasapi.domain.UserCommand
import no.falcon.wallasapi.repository.rowmapper.UserCommandRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class UserCommandsRepository(private val jdbcTemplate: JdbcTemplate) {

    fun insertPendingCommand(temperature: Int, startTime: LocalDateTime) {
        jdbcTemplate.update(
            "INSERT INTO public.commands (user_id, command, temperature, start_time, status) VALUES (?, ?, ?, ?, ?)",
            "unknown",
            "start",
            temperature,
            startTime,
            "pending"
        )
    }

    fun getUserCommands(): List<UserCommand> {
        return jdbcTemplate.query("SELECT * FROM public.commands", UserCommandRowMapper())
    }
}
