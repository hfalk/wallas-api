package no.falcon.wallasapi.resource

import mu.KotlinLogging
import org.springframework.http.HttpEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("twillio")
class TwillioResource {
    private val logger = KotlinLogging.logger {}

    @PostMapping("inbound")
    fun inbound(httpEntity: HttpEntity<String>) {
        logger.info { httpEntity.body }
        logger.info { httpEntity.headers }
    }
}
