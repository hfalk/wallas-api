package no.falcon.wallasapi.domain

data class ClickSendInboundSms(
    val timestamp: String,
    val to: String,
    val from: String,
    val body: String,
    val original_body: String,
    val original_message_id: String,
    val custom_string: String,
    val user_id: String,
    val subaccount_id: String,
    val message_id: String
)
