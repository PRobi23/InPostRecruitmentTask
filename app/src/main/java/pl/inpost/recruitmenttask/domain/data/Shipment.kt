package pl.inpost.recruitmenttask.domain.data

import java.time.ZonedDateTime

data class Shipment(
    val number: String,
    val shipmentType: String,
    val status: ShipmentStatus,
    val operationsHighlight: Boolean,
    val senderEmail: String?,
    val expiryDate: ZonedDateTime?,
    val storedDate: ZonedDateTime?,
    val pickUpDate: ZonedDateTime?
)