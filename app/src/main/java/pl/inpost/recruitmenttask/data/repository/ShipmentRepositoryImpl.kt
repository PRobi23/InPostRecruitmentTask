package pl.inpost.recruitmenttask.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import pl.inpost.recruitmenttask.R
import pl.inpost.recruitmenttask.core.analytics.Analytics
import pl.inpost.recruitmenttask.core.di.DefaultDispatcher
import pl.inpost.recruitmenttask.core.util.Response
import pl.inpost.recruitmenttask.core.util.UiText
import pl.inpost.recruitmenttask.data.local.dao.ShipmentsDao
import pl.inpost.recruitmenttask.data.remote.api.MockShipmentApi
import pl.inpost.recruitmenttask.domain.data.Shipment
import pl.inpost.recruitmenttask.domain.mapper.ShipmentMappers
import pl.inpost.recruitmenttask.domain.repository.ShipmentRepository
import java.io.IOException

class ShipmentRepositoryImpl(
    private val shipmentApi: MockShipmentApi,
    private val shipmentMappers: ShipmentMappers,
    private val analytics: Analytics,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val shipmentsDao: ShipmentsDao
) : ShipmentRepository {

    private var firstUse = true

    /**
     * {@inheritDoc}
     */
    override suspend fun getShipments(): Flow<Response<List<Shipment>>> = flow {
        emit(Response.Loading())
        delay(1000)

        fetchAndRefreshDatabase()
        val shipments = shipmentsDao.getAllActiveShipments()
            .map { shipmentMappers.shipmentEntityToShipmentMapper.map(it) }
        emit(Response.Success(shipments))

    }.flowOn(defaultDispatcher)

    override suspend fun archiveShipment(shipment: Shipment): Flow<Response<Unit>> = flow {
        emit(Response.Loading())
        try {
            val shipmentEntity =
                shipmentMappers.shipmentToShipmentEntityMapper.map(shipment).copy(archived = true)
            val existingShipmentEntity = shipmentsDao.getShipmentByNumber(shipment.number)

            shipmentsDao.update(shipmentEntity.copy(id = existingShipmentEntity.id))

            emit(Response.Success(Unit))
        } catch (e: Exception) {
            analytics.logError()
            emit(Response.Error<Unit>(UiText.StringResource(R.string.unknown_error)))
        }
    }

    private suspend fun FlowCollector<Response<List<Shipment>>>.fetchAndRefreshDatabase() {
        try {
            val shipmentDto = if (firstUse) {
                firstUse = false
                emptyList()
            } else {
                shipmentApi.response.shipments
            }
            val archivedShipments = shipmentsDao.getAllArchivedShipments()
            val shipmentEntities = shipmentDto.map {
                shipmentMappers.shipmentDtoToShipmentEntityMapper.map(it)
            }.filterNot { entity ->
                archivedShipments.any { archivedEntity ->
                    archivedEntity.number == entity.number
                }
            }
            shipmentsDao.deleteAllActiveShipments()
            shipmentsDao.insertShipments(shipmentEntities)
        } catch (e: IOException) {
            emit(Response.Error<List<Shipment>>(UiText.StringResource(R.string.no_internet)))
            analytics.logError()
        } catch (e: Exception) {
            emit(Response.Error<List<Shipment>>(UiText.StringResource(R.string.unknown_error)))
            analytics.logError()
        }
    }
}