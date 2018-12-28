package no.falcon.wallasapi.resource

import mu.KotlinLogging
import no.falcon.wallasapi.domain.TwillioInboundSms
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("twillio")
class TwillioResource {
    private val logger = KotlinLogging.logger {}

    @PostMapping(value = ["inbound"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun inbound(twillioInboundSms: TwillioInboundSms): ResponseEntity<Unit> {
        logger.info { twillioInboundSms }

        return ResponseEntity.noContent().build()
    }
}
