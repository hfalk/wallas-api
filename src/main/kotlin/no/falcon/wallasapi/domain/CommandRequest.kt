package no.falcon.wallasapi.domain

import java.time.LocalDateTime

data class CommandRequest(val temperature: Int?, val startTime: LocalDateTime?)
