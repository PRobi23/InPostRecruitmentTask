package pl.inpost.recruitmenttask.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.inpost.recruitmenttask.core.util.Response
import pl.inpost.recruitmenttask.domain.data.Shipment
import pl.inpost.recruitmenttask.domain.repository.ShipmentRepository
import javax.inject.Inject

class GetShipmentsUseCase @Inject constructor(private val shipmentRepository: ShipmentRepository) {

    suspend operator fun invoke(): Flow<Response<List<Shipment>>> {
        return shipmentRepository.getShipments()
    }
}