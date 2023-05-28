package pl.inpost.recruitmenttask.domain.usecase

import pl.inpost.recruitmenttask.domain.data.Shipment
import pl.inpost.recruitmenttask.presentation.shipmentList.ShipmentItem
import pl.inpost.recruitmenttask.presentation.shipmentList.ShipmentType
import javax.inject.Inject

class GroupShipmentsByOperationHighlightUseCase @Inject constructor() {

    operator fun invoke(shipments: List<Shipment>): List<ShipmentItem> {
        return shipments.groupBy { it.operationsHighlight }.map {
            ShipmentItem(
                shipmentType = if (it.key) ShipmentType.READY_TO_PICK_UP else ShipmentType.OTHER,
                shipments = it.value
            )
        }
    }
}