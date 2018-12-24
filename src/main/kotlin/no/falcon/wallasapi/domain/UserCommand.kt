package no.falcon.wallasapi.domain

import java.time.LocalDateTime

data class UserCommand(
    val id: Int?,
    val userId: String?,
    val type: CommandType,
    val temperature: Int,
    val startTime: LocalDateTime,
    val status: CommandStatus,
    val messageSid: String?
)
