package no.falcon.wallasapi

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber

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
    fun start() {

    }

    @PostMapping("change")
    fun change() {

    }

    @PostMapping("stop")
    fun stop() {

    }
}
