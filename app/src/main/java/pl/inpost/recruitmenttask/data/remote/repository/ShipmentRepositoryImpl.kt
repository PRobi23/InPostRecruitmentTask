package pl.inpost.recruitmenttask.data.remote.repository

import kotlinx.coroutines.delay
import pl.inpost.recruitmenttask.data.remote.api.MockShipmentApi
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentDTO
import pl.inpost.recruitmenttask.domain.data.Shipment
import pl.inpost.recruitmenttask.domain.mapper.Mapper
import pl.inpost.recruitmenttask.domain.repository.ShipmentRepository

class ShipmentRepositoryImpl(
    private val mockShipmentApi: MockShipmentApi,
    private val shipmentDtoToShipmentMapper: Mapper<ShipmentDTO, Shipment>
) : ShipmentRepository {

    private var firstUse = true

    /**
     * {@inheritDoc}
     */
    override suspend fun getShipments(): List<Shipment> {
        delay(1000)
        return if (firstUse) {
            firstUse = false
            emptyList()
        } else {
            mockShipmentApi.response.shipments.map {
                shipmentDtoToShipmentMapper.map(it)
            }
        }
    }
}