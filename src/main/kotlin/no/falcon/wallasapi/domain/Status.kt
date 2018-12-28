package no.falcon.wallasapi.domain

import java.time.LocalDateTime

data class Status(
    val id: String,
    val createdTime: LocalDateTime,
    val content: StatusContent,
    val messageId: String,
    val pushNotificationId: String?
)
