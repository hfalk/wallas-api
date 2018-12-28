package no.falcon.wallasapi.repository

import no.falcon.wallasapi.domain.Status
import no.falcon.wallasapi.repository.rowmapper.StatusRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class StatusRepository(private val jdbcTemplate: JdbcTemplate) {
    fun insertStatus(rawStatusString: String, fromPhoneNumber: String, messageId: String): String {
        val id = UUID.randomUUID().toString()

        jdbcTemplate.update(
            "INSERT INTO public.status (id, raw_value, from_phone_number, message_id) VALUES (?, ?, ?, ?)",
            id,
            rawStatusString,
            fromPhoneNumber,
            messageId
        )

        return id
    }

    fun getStatuses(): List<Status> {
        return jdbcTemplate.query("SELECT * FROM public.status", StatusRowMapper())
    }

    fun updatePushNotificationId(statusId: String, pushNotificationId: String) {
        jdbcTemplate.update(
            "UPDATE public.status SET push_notification_id = ? WHERE id = ?",
            pushNotificationId,
            statusId
        )
    }
}
