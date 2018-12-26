package no.falcon.wallasapi.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "app-center")
class AppCenterProperties {
    lateinit var apiToken: String
    lateinit var baseUrl: String
    lateinit var ownerName: String
    lateinit var iosAppName: String
    lateinit var androidAppName: String
}
