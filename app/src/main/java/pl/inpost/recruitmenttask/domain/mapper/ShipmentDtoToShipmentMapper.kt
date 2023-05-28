package pl.inpost.recruitmenttask.domain.mapper

import pl.inpost.recruitmenttask.data.remote.dto.ShipmentDTO
import pl.inpost.recruitmenttask.domain.data.DateType
import pl.inpost.recruitmenttask.domain.data.Shipment
import pl.inpost.recruitmenttask.domain.data.ShipmentStatus
import java.lang.Exception
import java.time.ZonedDateTime

class ShipmentDtoToShipmentMapper : Mapper<ShipmentDTO, Shipment> {

    /***
     * Map shipment dto object to shipment.
     * VALIDATE:
     * Date to show can show only one date, if it's available.
     * The order of the date:
     * pickupDate
     * expireDate
     * storedDate
     * The first available will be shown.
     */
    override fun map(input: ShipmentDTO): Shipment = Shipment(
        number = input.number,
        shipmentType = input.shipmentType,
        status = getShipmentStatusByValue(input.status.uppercase()),
        operationsHighlight = input.operations.highlight,
        senderEmail = input.sender?.email,
        expiryDate = input.expiryDate,
        storedDate = input.storedDate,
        pickUpDate = input.pickUpDate
    )

    fun getDateType(
        expiryDate: ZonedDateTime?,
        storedDate: ZonedDateTime?,
        pickUpDate: ZonedDateTime?
    ): DateType {
        return when {
            pickUpDate != null -> DateType.PICK_UP
            expiryDate != null -> DateType.EXPIRY
            storedDate != null -> DateType.STORED
            else -> DateType.NO_DATE
        }
    }

    private fun getShipmentStatusByValue(value: String): ShipmentStatus {
        return try {
            ShipmentStatus.valueOf(value)
        } catch (e: Exception) {
            ShipmentStatus.UNKNOWN
        }
    }
}