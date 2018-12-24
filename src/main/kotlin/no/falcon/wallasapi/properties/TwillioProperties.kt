package no.falcon.wallasapi.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "twillio")
class TwillioProperties {
    lateinit var accountSid: String
    lateinit var authToken: String
}
