package no.falcon.wallasapi.resource

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import no.falcon.wallasapi.domain.CommandRequest
import no.falcon.wallasapi.properties.TwillioProperties
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("commands")
class CommandsResource(private val twillioProperties: TwillioProperties) {

    @GetMapping("test")
    fun test() {
        Twilio.init(twillioProperties.accountSid, twillioProperties.authToken)

        val message = Message
            .creator(PhoneNumber("+4795184532"), PhoneNumber("+4759446916"), "Test")
            .create()
    }

    @PostMapping("start")
    fun start(@RequestBody commandRequest: CommandRequest) {

    }

    @PostMapping("change")
    fun change(@RequestBody commandRequest: CommandRequest) {

    }

    @PostMapping("stop")
    fun stop(@RequestBody commandRequest: CommandRequest) {

    }
}
