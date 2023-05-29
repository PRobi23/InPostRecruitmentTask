package pl.inpost.recruitmenttask.presentation.shipmentList

import pl.inpost.recruitmenttask.core.util.UiText

/***
 * Class that holds the state of the shipment list
 */
data class ShipmentListState(
    val isLoading: Boolean = false,
    val shipmentItems: List<ShipmentItem>? = emptyList(),
    val error: UiText? = null
)