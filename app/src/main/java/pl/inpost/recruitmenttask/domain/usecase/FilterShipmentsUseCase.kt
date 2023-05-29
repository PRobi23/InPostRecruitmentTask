package pl.inpost.recruitmenttask.domain.usecase

import pl.inpost.recruitmenttask.presentation.shipmentList.ShipmentItem
import pl.inpost.recruitmenttask.presentation.shipmentList.ShipmentType
import javax.inject.Inject

class FilterShipmentsUseCase @Inject constructor() {

    operator fun invoke(shipmentItems: List<ShipmentItem>, filter: Filter): List<ShipmentItem> {
        return when (filter) {
            Filter.READY_TO_PICK_UP -> shipmentItems.filter { it.shipmentType == ShipmentType.READY_TO_PICK_UP }
            Filter.OTHER -> shipmentItems.filter { it.shipmentType == ShipmentType.OTHER }
            Filter.ALL -> shipmentItems
        }
    }

    enum class Filter {
        READY_TO_PICK_UP, OTHER, ALL
    }
}