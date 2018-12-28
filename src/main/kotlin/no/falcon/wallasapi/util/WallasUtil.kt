package no.falcon.wallasapi.util

import no.falcon.wallasapi.domain.StatusContent

class WallasUtil {
    companion object {
        private fun parseStatusResponseLine(line: String) = line.split(":")[1].trim()

        fun parseStatusResponseString(rawStatusString: String): StatusContent {
            val parsedString = rawStatusString.replace('\r', '|')
            val lines = parsedString.split('|')

            return StatusContent(
                rawStatusString,
                parseStatusResponseLine(lines[0]),
                parseStatusResponseLine(lines[2]),
                parseStatusResponseLine(lines[3]),
                parseStatusResponseLine(lines[4])
            )
        }
    }
}
