package pl.inpost.recruitmenttask.data.repository.fake

import pl.inpost.recruitmenttask.ShipmentGenerator
import pl.inpost.recruitmenttask.domain.data.Shipment
import pl.inpost.recruitmenttask.domain.repository.ShipmentRepository

class FakeShipmentRepository : ShipmentRepository {

    override suspend fun getShipments(): List<Shipment> {
        val shipments = mutableListOf<Shipment>()
        repeat(6) { index ->
            val shipmentDTO = ShipmentGenerator.mockShipmentNetwork(index.toString())
            val shipment = Shipment(
                number = shipmentDTO.number,
                shipmentType = shipmentDTO.shipmentType,
                status = shipmentDTO.status
            )
            shipments.add(shipment)
        }
        return shipments
    }

}