package no.falcon.wallasapi.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "features")
class FeaturesProperties {
    var smsEnabled: Boolean = true
}
