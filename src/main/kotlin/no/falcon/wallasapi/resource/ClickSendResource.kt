package no.falcon.wallasapi.resource

import no.falcon.wallasapi.domain.ClickSendInboundSms
import no.falcon.wallasapi.service.StatusService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("click-send")
class ClickSendResource(
    private val statusService: StatusService,
) {
    @PostMapping(value = ["inbound"], consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun inbound(clickSendInboundSms: ClickSendInboundSms): ResponseEntity<Unit> {
//        statusService.insertStatus(twilioInboundSms.Body, twilioInboundSms.MessageSid)
        System.err.println("inbound sms: $clickSendInboundSms")
        return ResponseEntity.noContent().build()
    }
}
