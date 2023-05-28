package pl.inpost.recruitmenttask.domain.usecase

import pl.inpost.recruitmenttask.domain.data.Shipment
import pl.inpost.recruitmenttask.domain.data.ShipmentComparator
import java.util.Collections
import javax.inject.Inject

class OrderShipmentsUseCase @Inject constructor(private val shipmentComparator: ShipmentComparator) {

    operator fun invoke(shipments: List<Shipment>): List<Shipment> {
        val shipmentComparator = ShipmentComparator()
        Collections.sort(shipments, shipmentComparator)
        return shipments
    }
}