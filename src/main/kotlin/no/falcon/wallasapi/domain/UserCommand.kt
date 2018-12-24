package no.falcon.wallasapi.domain

import java.time.LocalDateTime

data class UserCommand(
    val id: Int,
    val userId: String,
    val command: String,
    val temperature: Int,
    val startTime: LocalDateTime,
    val status: String,
    val messageSid: String?
)
