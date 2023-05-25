package pl.inpost.recruitmenttask.domain.repository

import pl.inpost.recruitmenttask.domain.data.Shipment

interface ShipmentRepository {

    /***
     * Returns the shipments.
     * @see Shipment
     */
    suspend fun getShipments(): List<Shipment>
}
