package no.falcon.wallasapi.resource

import mu.KotlinLogging
import no.falcon.wallasapi.domain.TwillioInboundSms
import no.falcon.wallasapi.service.StatusService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("twillio")
class TwillioResource(
    private val statusService: StatusService,
    private val request: HttpServletRequest
) {
    private val logger = KotlinLogging.logger {}

    @PostMapping(value = ["inbound"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun inbound(twillioInboundSms: TwillioInboundSms) {
        validateTwillioRequest()
        statusService.insertStatus(twillioInboundSms.Body, twillioInboundSms.MessageSid)
    }

    fun validateTwillioRequest() {
        logger.info { "***********" }
        logger.info { request.contextPath }
        logger.info { request.queryString }
        logger.info { request.requestURI }
        logger.info { request.pathInfo }
        logger.info { request.servletPath }
        logger.info { "***********" }
    }
}
