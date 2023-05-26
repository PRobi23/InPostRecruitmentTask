package pl.inpost.recruitmenttask.domain.usecase

import pl.inpost.recruitmenttask.domain.data.Shipment
import javax.inject.Inject

class GroupShipmentsByOperationHighlightUseCase @Inject constructor() {

    operator fun invoke(shipments: List<Shipment>): Map<Boolean, List<Shipment>> {
        return shipments.groupBy { it.operationsHighlight }
    }
}