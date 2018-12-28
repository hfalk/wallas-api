package no.falcon.wallasapi.resource

import no.falcon.wallasapi.domain.TwillioInboundSms
import no.falcon.wallasapi.properties.TwillioProperties
import no.falcon.wallasapi.service.StatusService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import com.twilio.security.RequestValidator
import org.springframework.http.ResponseEntity


@RestController
@RequestMapping("twillio")
class TwillioResource(
    private val statusService: StatusService,
    private val request: HttpServletRequest,
    private val twillioProperties: TwillioProperties
) {
    @PostMapping(value = ["inbound"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun inbound(twillioInboundSms: TwillioInboundSms): ResponseEntity<Unit> {
        if (!twillioRequestIsValid()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        statusService.insertStatus(twillioInboundSms.Body, twillioInboundSms.MessageSid)

        return ResponseEntity.noContent().build()
    }

    fun twillioRequestIsValid(): Boolean {
        val authToken = twillioProperties.authToken

        val validator = RequestValidator(authToken)

        val url = request.requestURL.toString()
        val params = request.parameterMap.map { prop -> prop.key to prop.value[0] }.toMap()
        val twilioSignature = request.getHeader("X-Twilio-Signature")

        return validator.validate(url, params, twilioSignature)
    }
}
