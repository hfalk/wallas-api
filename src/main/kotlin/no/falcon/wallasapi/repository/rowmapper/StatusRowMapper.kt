package no.falcon.wallasapi.repository.rowmapper

import no.falcon.wallasapi.domain.Status
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class StatusRowMapper : RowMapper<Status> {
    override fun mapRow(resultSet: ResultSet, rowNumber: Int): Status {
        return Status(
            resultSet.getString("id"),
            resultSet.getString("raw_value"),
            resultSet.getString("from_phone_number"),
            resultSet.getString("message_id"),
            resultSet.getString("push_notification_id")
        )
    }
}
