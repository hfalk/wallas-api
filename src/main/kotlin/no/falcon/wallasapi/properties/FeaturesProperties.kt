package no.falcon.wallasapi.properties

import no.falcon.wallasapi.domain.SmsProvider
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "features")
class FeaturesProperties {
    lateinit var smsProvider: SmsProvider
}
