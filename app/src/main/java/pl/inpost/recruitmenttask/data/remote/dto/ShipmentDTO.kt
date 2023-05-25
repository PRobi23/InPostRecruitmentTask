package pl.inpost.recruitmenttask.data.remote.dto

import java.time.ZonedDateTime

data class ShipmentDTO(
    val number: String,
    val shipmentType: String,
    val status: String,
    val eventLog: List<EventLogNetworkDTO>,
    val openCode: String?,
    val expiryDate: ZonedDateTime?,
    val storedDate: ZonedDateTime?,
    val pickUpDate: ZonedDateTime?,
    val receiver: CustomerNetworkDTO?,
    val sender: CustomerNetworkDTO?,
    val operations: OperationsNetworkDTO
)
