package no.falcon.wallasapi.domain

data class TwilioInboundSms(
    val ToCountry: String,
    val ToState: String,
    val SmsMessageSid: String,
    val NumMedia: String,
    val ToCity: String,
    val FromZip: String,
    val SmsSid: String,
    val FromState: String,
    val SmsStatus: String,
    val FromCity: String,
    val Body: String,
    val FromCountry: String,
    val To: String,
    val MessagingServiceSid: String,
    val ToZip: String,
    val NumSegments: String,
    val MessageSid: String,
    val AccountSid: String,
    val From: String,
    val ApiVersion: String
)
