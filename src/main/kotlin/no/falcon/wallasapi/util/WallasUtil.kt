package no.falcon.wallasapi.util

import no.falcon.wallasapi.domain.StatusContent

class WallasUtil {
    companion object {
        private fun parseStatusResponseLine(line: String) = line.split(":")[1].trim()

        fun parseStatusResponseString(rawStatusString: String): StatusContent {
            val parsedString = rawStatusString.replace('\r', '|')
            val lines = parsedString.split('|')

            val setTemp = parseStatusResponseLine(lines[2]).replace("C", "")
            val readTemp = parseStatusResponseLine(lines[3]).replace("C", "")
            val volt = parseStatusResponseLine(lines[4]).replace("V", "")

            return StatusContent(
                rawStatusString,
                parseStatusResponseLine(lines[0]),
                setTemp.toDouble(),
                readTemp.toDouble(),
                volt.toDouble()
            )
        }
    }
}
