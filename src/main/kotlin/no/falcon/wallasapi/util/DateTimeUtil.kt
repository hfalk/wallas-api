package no.falcon.wallasapi.util

import java.time.LocalDateTime
import java.time.ZoneOffset

class DateTimeUtil {
    companion object {
        fun now() = LocalDateTime.now(ZoneOffset.UTC)
    }
}
