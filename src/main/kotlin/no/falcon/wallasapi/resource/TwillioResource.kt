package no.falcon.wallasapi.resource

import no.falcon.wallasapi.domain.TwillioInboundSms
import no.falcon.wallasapi.service.StatusService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("twillio")
class TwillioResource(private val statusService: StatusService) {
    @PostMapping(value = ["inbound"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun inbound(twillioInboundSms: TwillioInboundSms) {
        statusService.insertStatus(twillioInboundSms.Body, twillioInboundSms.From, twillioInboundSms.MessageSid)
    }
}
