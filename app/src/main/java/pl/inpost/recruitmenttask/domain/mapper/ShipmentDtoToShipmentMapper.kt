package pl.inpost.recruitmenttask.domain.mapper

import pl.inpost.recruitmenttask.data.remote.dto.ShipmentDTO
import pl.inpost.recruitmenttask.domain.data.Shipment

class ShipmentDtoToShipmentMapper : Mapper<ShipmentDTO, Shipment> {

    override fun map(input: ShipmentDTO): Shipment = Shipment(
        number = input.number,
        shipmentType = input.shipmentType,
        status = input.status,
        operationsHighlight = input.operations.highlight
    )
}