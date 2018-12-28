package no.falcon.wallasapi.resource

import no.falcon.wallasapi.service.StatusService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("statuses")
class StatusResource(private val statusService: StatusService) {
    @GetMapping
    fun getStatuses() = statusService.getStatuses()
}
