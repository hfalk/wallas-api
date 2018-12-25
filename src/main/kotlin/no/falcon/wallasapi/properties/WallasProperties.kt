package no.falcon.wallasapi.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "wallas")
class WallasProperties {
    lateinit var phoneNumber: String
    lateinit var pinCode: String
}
