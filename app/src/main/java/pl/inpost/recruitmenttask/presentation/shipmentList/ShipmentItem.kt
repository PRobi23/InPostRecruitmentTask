package pl.inpost.recruitmenttask.presentation.shipmentList

import pl.inpost.recruitmenttask.domain.data.Shipment

data class ShipmentItem(
    val shipmentType: ShipmentType,
    val shipments: List<Shipment>
)