package no.falcon.wallasapi

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("twillio")
class TwillioProperties {
    lateinit var accountSid: String
    lateinit var authToken: String
}
