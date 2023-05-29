package pl.inpost.recruitmenttask.domain.mapper

import pl.inpost.recruitmenttask.core.dateTime.DateTimeUtils
import pl.inpost.recruitmenttask.data.local.entity.ShipmentEntity
import pl.inpost.recruitmenttask.domain.data.Shipment

class ShipmentToShipmentEntityMapper(private val dateTimeUtils: DateTimeUtils) :
    Mapper<Shipment, ShipmentEntity> {

    override fun map(input: Shipment): ShipmentEntity = ShipmentEntity(
        number = input.number,
        shipmentType = input.shipmentType,
        status = input.status.name,
        operationsHighlight = input.operationsHighlight,
        senderEmail = input.senderEmail,
        expiryDate = input.expiryDate?.let { dateTimeUtils.zonedDateTimeToEpochMilliseconds(it) },
        storedDate = input.storedDate?.let { dateTimeUtils.zonedDateTimeToEpochMilliseconds(it) },
        pickUpDate = input.pickUpDate?.let { dateTimeUtils.zonedDateTimeToEpochMilliseconds(it) },
        archived = input.archived
    )
}