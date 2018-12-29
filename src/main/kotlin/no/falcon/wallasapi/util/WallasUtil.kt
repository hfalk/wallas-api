package no.falcon.wallasapi.util

import no.falcon.wallasapi.domain.StatusContent
import no.falcon.wallasapi.domain.WallasStatus
import java.lang.Exception

class WallasUtil {
    companion object {
        private fun parseStatusResponseLine(line: String) = line.split(":")[1].trim()

        fun parseStatusResponseString(rawStatusString: String): StatusContent {
            return try {
                val parsedString = rawStatusString.replace('\r', '|')
                val lines = parsedString.split('|')

                if (parsedString.contains("Wallas error")) {
                    StatusContent(
                        rawStatusString,
                        WallasStatus.ERROR,
                        lines[1],
                        null,
                        null,
                        null
                    )
                } else {
                    val setTemp = parseStatusResponseLine(lines[2]).replace("C", "")
                    val readTemp = parseStatusResponseLine(lines[3]).replace("C", "")
                    val volt = parseStatusResponseLine(lines[4]).replace("V", "")

                    StatusContent(
                        rawStatusString,
                        WallasStatus.OK,
                        parseStatusResponseLine(lines[0]),
                        setTemp.toDouble(),
                        readTemp.toDouble(),
                        volt.toDouble()
                    )
                }
            } catch (ex: Exception) {
                StatusContent(rawStatusString, WallasStatus.UNKNOWN, null, null, null, null)
            }
        }
    }
}
