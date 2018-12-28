package no.falcon.wallasapi.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "twilio")
class TwilioProperties {
    lateinit var accountSid: String
    lateinit var authToken: String
    lateinit var serviceSid: String
}
