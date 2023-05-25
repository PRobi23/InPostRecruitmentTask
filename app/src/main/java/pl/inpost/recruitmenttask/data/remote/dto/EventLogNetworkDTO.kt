package pl.inpost.recruitmenttask.data.remote.dto

import java.time.ZonedDateTime

data class EventLogNetworkDTO(
    val name: String,
    val date: ZonedDateTime
)
