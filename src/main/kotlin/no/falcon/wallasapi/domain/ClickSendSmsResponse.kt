package no.falcon.wallasapi.domain

import kotlinx.serialization.Serializable

@Serializable
data class ClickSendSmsResponse(
    val data: Data
) {
    @Serializable
    data class Data(
        val total_price: Float,
        val messages: List<Message>
    ) {
        @Serializable
        data class Message(
            val message_id: String,
            val status: String
        )
    }
}

