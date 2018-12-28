package no.falcon.wallasapi.resource

import mu.KotlinLogging
import no.falcon.wallasapi.domain.TwillioInboundSms
import no.falcon.wallasapi.properties.TwillioProperties
import no.falcon.wallasapi.service.StatusService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import com.twilio.security.RequestValidator



@RestController
@RequestMapping("twillio")
class TwillioResource(
    private val statusService: StatusService,
    private val request: HttpServletRequest,
    private val twillioProperties: TwillioProperties
) {
    private val logger = KotlinLogging.logger {}

    @PostMapping(value = ["inbound"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun inbound(twillioInboundSms: TwillioInboundSms) {
        validateTwillioRequest()
        statusService.insertStatus(twillioInboundSms.Body, twillioInboundSms.MessageSid)
    }

    fun validateTwillioRequest() {
        val authToken = twillioProperties.authToken

        val validator = RequestValidator(authToken)

        val url = request.requestURL.toString()
        val params = request.parameterMap.map { prop -> prop.key to prop.value[0] }.toMap()
        val twilioSignature = request.getHeader("X-Twilio-Signature")

        val isValid = validator.validate(url, params, twilioSignature)

        logger.info { isValid }
    }
}
