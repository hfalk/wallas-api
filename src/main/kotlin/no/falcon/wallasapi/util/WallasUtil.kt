package no.falcon.wallasapi.util

import mu.KotlinLogging
import no.falcon.wallasapi.domain.StatusContent
import java.lang.Exception

class WallasUtil {
    companion object {
        private val logger = KotlinLogging.logger {}

        private fun parseStatusResponseLine(line: String) = line.split(":")[1].trim()

        fun parseStatusResponseString(rawStatusString: String): StatusContent {
            return try {
                val parsedString = rawStatusString.replace('\r', '|')
                val lines = parsedString.split('|')

                val setTemp = parseStatusResponseLine(lines[2]).replace("C", "")
                val readTemp = parseStatusResponseLine(lines[3]).replace("C", "")
                val volt = parseStatusResponseLine(lines[4]).replace("V", "")

                StatusContent(
                    rawStatusString,
                    parseStatusResponseLine(lines[0]),
                    setTemp.toDouble(),
                    readTemp.toDouble(),
                    volt.toDouble()
                )
            } catch (ex: Exception) {
                logger.warn { "Failed to parse status response string [value=$rawStatusString]" }
                StatusContent(rawStatusString, null, null, null, null)
            }
        }
    }
}
