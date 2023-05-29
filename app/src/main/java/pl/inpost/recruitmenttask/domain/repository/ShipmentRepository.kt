package pl.inpost.recruitmenttask.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.inpost.recruitmenttask.core.util.Response
import pl.inpost.recruitmenttask.domain.data.Shipment

/***
 * Shipment repository interface. This interface is responsible for getting shipment related values.
 */
interface ShipmentRepository {

    /**
     * Get shipments from database. First loading state is emitted and after that if the database call succeeds the
     * shipment are emitted if it fails then error is propagated.
     * @return List of shipments in flow
     */
    suspend fun getShipments(): Flow<Response<List<Shipment>>>
}
