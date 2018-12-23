package no.falcon.wallasapi

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("commands")
class CommandsResource {
    @PostMapping("start")
    fun start() {

    }

    @PostMapping("change")
    fun change() {

    }

    @PostMapping("stop")
    fun stop() {

    }
}
