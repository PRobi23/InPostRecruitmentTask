package pl.inpost.recruitmenttask.domain.mapper

import pl.inpost.recruitmenttask.core.dateTime.DateTimeUtils
import pl.inpost.recruitmenttask.data.local.entity.ShipmentEntity
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentDTO
import pl.inpost.recruitmenttask.domain.data.DateType
import pl.inpost.recruitmenttask.domain.data.Shipment
import pl.inpost.recruitmenttask.domain.data.ShipmentStatus
import java.lang.Exception
import java.time.ZonedDateTime

class ShipmentEntityToShipmentMapper(private val dateTimeUtils: DateTimeUtils) :
    Mapper<ShipmentEntity, Shipment> {

    /***
     * Map shipment dto object to shipment.
     */
    override fun map(input: ShipmentEntity): Shipment = Shipment(
        number = input.number,
        shipmentType = input.shipmentType,
        status = getShipmentStatusByValue(input.status.uppercase()),
        operationsHighlight = input.operationsHighlight,
        senderEmail = input.senderEmail,
        expiryDate = input.expiryDate?.let { dateTimeUtils.epochMillisecondsToZonedDateTime(it) },
        storedDate = input.storedDate?.let { dateTimeUtils.epochMillisecondsToZonedDateTime(it) },
        pickUpDate = input.pickUpDate?.let { dateTimeUtils.epochMillisecondsToZonedDateTime(it) },
        archived = input.archived
    )

    private fun getShipmentStatusByValue(value: String): ShipmentStatus {
        return try {
            ShipmentStatus.valueOf(value)
        } catch (e: Exception) {
            ShipmentStatus.UNKNOWN
        }
    }
}