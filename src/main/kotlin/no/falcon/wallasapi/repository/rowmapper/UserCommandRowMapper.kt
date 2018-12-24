package no.falcon.wallasapi.repository.rowmapper

import no.falcon.wallasapi.domain.CommandStatus
import no.falcon.wallasapi.domain.CommandType
import no.falcon.wallasapi.domain.UserCommand
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class UserCommandRowMapper : RowMapper<UserCommand> {
    override fun mapRow(resultSet: ResultSet, rowNumber: Int): UserCommand {
        return UserCommand(
            resultSet.getInt("id"),
            resultSet.getString("user_id"),
            CommandType.valueOf(resultSet.getString("command")),
            resultSet.getInt("temperature"),
            resultSet.getTimestamp("start_time").toLocalDateTime(),
            CommandStatus.valueOf(resultSet.getString("status")),
            resultSet.getString("message_sid")
        )
    }
}
