package no.falcon.wallasapi.domain

data class Status(
    val id: String,
    val content: StatusContent,
    val fromPhoneNumber: String,
    val messageId: String,
    val pushNotificationId: String?
)
