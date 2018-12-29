package no.falcon.wallasapi.domain

data class StatusContent(
    val rawValue: String,
    val status: WallasStatus,
    val message: String?,
    val setTemp: Double?,
    val readTemp: Double?,
    val volt: Double?
)
