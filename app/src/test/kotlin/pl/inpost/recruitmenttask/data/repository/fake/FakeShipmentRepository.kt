package pl.inpost.recruitmenttask.data.repository.fake

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import pl.inpost.recruitmenttask.ShipmentGenerator
import pl.inpost.recruitmenttask.core.util.Response
import pl.inpost.recruitmenttask.domain.data.Shipment
import pl.inpost.recruitmenttask.domain.data.ShipmentStatus
import pl.inpost.recruitmenttask.domain.repository.ShipmentRepository

class FakeShipmentRepository : ShipmentRepository {

    override suspend fun getShipments(): Flow<Response<List<Shipment>>> {
        val shipments = mutableListOf<Shipment>()
        repeat(6) { index ->
            val shipmentDTO = ShipmentGenerator.createShipmentFromNetwork(index.toLong())
            val shipment = Shipment(
                number = shipmentDTO.number,
                shipmentType = shipmentDTO.shipmentType,
                status = ShipmentStatus.valueOf(shipmentDTO.status),
                operationsHighlight = true,
                senderEmail = "name@email.com",
                expiryDate = shipmentDTO.expiryDate,
                pickUpDate = shipmentDTO.pickUpDate,
                storedDate = shipmentDTO.storedDate
            )
            shipments.add(shipment)
        }
        return flowOf(Response.Success(shipments))
    }
}