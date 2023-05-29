package pl.inpost.recruitmenttask.domain.mapper

import pl.inpost.recruitmenttask.data.local.entity.ShipmentEntity
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentDTO
import pl.inpost.recruitmenttask.domain.data.Shipment
import javax.inject.Inject

data class ShipmentMappers @Inject constructor(
    val shipmentEntityToShipmentMapper: Mapper<ShipmentEntity, Shipment>,
    val shipmentToShipmentEntityMapper: Mapper<Shipment, ShipmentEntity>,
    val shipmentDtoToShipmentEntityMapper: Mapper<ShipmentDTO, ShipmentEntity>,
)