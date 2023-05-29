package pl.inpost.recruitmenttask.domain.mapper

import pl.inpost.recruitmenttask.core.dateTime.DateTimeUtils
import pl.inpost.recruitmenttask.data.local.entity.ShipmentEntity
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentDTO

class ShipmentDtoToShipmentEntityMapper(private val dateTimeUtils: DateTimeUtils) :
    Mapper<ShipmentDTO, ShipmentEntity> {

    override fun map(input: ShipmentDTO): ShipmentEntity = ShipmentEntity(
        number = input.number,
        shipmentType = input.shipmentType,
        status = input.status,
        operationsHighlight = input.operations.highlight,
        senderEmail = input.sender?.email,
        expiryDate = input.expiryDate?.let { dateTimeUtils.zonedDateTimeToEpochMilliseconds(it) },
        storedDate = input.storedDate?.let { dateTimeUtils.zonedDateTimeToEpochMilliseconds(it) },
        pickUpDate = input.pickUpDate?.let { dateTimeUtils.zonedDateTimeToEpochMilliseconds(it) }
    )
}