package no.falcon.wallasapi.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "click-send")
class ClickSendProperties {
    lateinit var userName: String
    lateinit var apiKey: String
}
