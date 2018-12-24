package no.falcon.wallasapi.domain

import java.time.LocalDateTime

data class UserCommand(
    val id: Int,
    val userId: String?,
    val createdTime: LocalDateTime,
    val startTime: LocalDateTime,
    val finishedTime: LocalDateTime?,
    val type: CommandType,
    val temperature: Int,
    val status: CommandStatus,
    val messageSid: String?
)
