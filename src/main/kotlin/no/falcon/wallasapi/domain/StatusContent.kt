package no.falcon.wallasapi.domain

data class StatusContent(
    val rawValue: String,
    val heaterStatus: String,
    val setTemp: String,
    val readTemp: String,
    val volt: String
)
