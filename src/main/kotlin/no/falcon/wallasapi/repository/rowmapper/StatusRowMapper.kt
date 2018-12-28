package no.falcon.wallasapi.repository.rowmapper

import no.falcon.wallasapi.domain.Status
import no.falcon.wallasapi.util.WallasUtil
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class StatusRowMapper : RowMapper<Status> {
    override fun mapRow(resultSet: ResultSet, rowNumber: Int): Status {
        return Status(
            resultSet.getString("id"),
            resultSet.getTimestamp("created_time").toLocalDateTime(),
            WallasUtil.parseStatusResponseString(resultSet.getString("raw_value")),
            resultSet.getString("message_id"),
            resultSet.getString("push_notification_id")
        )
    }
}
