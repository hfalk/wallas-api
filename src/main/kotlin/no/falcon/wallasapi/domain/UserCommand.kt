package no.falcon.wallasapi.domain

import java.time.LocalDateTime

data class UserCommand(
    val id: String,
    val userId: String?,
    val createdTime: LocalDateTime,
    val startTime: LocalDateTime,
    val lastUpdatedTime: LocalDateTime,
    val type: CommandType,
    val temperature: Int,
    val status: CommandStatus,
    val messageId: String?,
    val pushNotificationId: String?
)
