package no.falcon.wallasapi.repository

import no.falcon.wallasapi.domain.CommandStatus
import no.falcon.wallasapi.domain.CommandType
import no.falcon.wallasapi.domain.UserCommand
import no.falcon.wallasapi.repository.rowmapper.UserCommandRowMapper
import no.falcon.wallasapi.util.DateTimeUtil
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
class UserCommandsRepository(private val jdbcTemplate: JdbcTemplate) {

    fun insertWaitingCommand(userId: String, type: CommandType, startTime: LocalDateTime, temperature: Int?): String {
        val id = UUID.randomUUID().toString()

        jdbcTemplate.update(
            "INSERT INTO public.user_commands (id, user_id, created_time, last_updated_time, type, temperature, start_time, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            id,
            userId,
            DateTimeUtil.now(),
            DateTimeUtil.now(),
            type.name,
            temperature,
            startTime,
            CommandStatus.WAITING.name
        )

        return id
    }

    fun getCommands(): List<UserCommand> {
        return jdbcTemplate.query("SELECT * FROM public.user_commands", UserCommandRowMapper())
    }

    fun deleteCommand(id: String) {
        jdbcTemplate.update("DELETE FROM public.user_commands WHERE id = ?", id)
    }

    fun updateCommandStatus(id: String, status: CommandStatus) {
        jdbcTemplate.update(
            "UPDATE public.user_commands SET status = ?, last_updated_time = ? WHERE id = ?",
            status.name,
            DateTimeUtil.now(),
            id
        )
    }

    fun updateMessageId(id: String, messageId: String) {
        jdbcTemplate.update(
            "UPDATE public.user_commands SET message_id = ? WHERE id = ?",
            messageId,
            id
        )
    }

    fun updatePushNotificationId(id: String, pushNotificationId: String) {
        jdbcTemplate.update(
            "UPDATE public.user_commands SET push_notification_id = ? WHERE id = ?",
            pushNotificationId,
            id
        )
    }

    fun getWaitingCommands(): List<UserCommand> {
        return jdbcTemplate.query(
            "SELECT * FROM public.user_commands WHERE status = ?",
            UserCommandRowMapper(),
            CommandStatus.WAITING.name
        )
    }
}
