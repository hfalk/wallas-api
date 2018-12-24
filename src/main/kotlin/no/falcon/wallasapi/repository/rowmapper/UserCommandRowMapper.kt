package no.falcon.wallasapi.repository.rowmapper

import no.falcon.wallasapi.domain.UserCommand
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.time.LocalDateTime

class UserCommandRowMapper : RowMapper<UserCommand> {
    override fun mapRow(resultSet: ResultSet, rowNumber: Int): UserCommand {
        return UserCommand(
            resultSet.getInt("id"),
            resultSet.getString("user_id"),
            resultSet.getString("command"),
            resultSet.getInt("temperature"),
            LocalDateTime.now(),
            resultSet.getString("status"),
            resultSet.getString("message_sid")
        )
    }
}
