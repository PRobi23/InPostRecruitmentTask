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
import pl.inpost.recruitmenttask.data.local.entity.ShipmentEntity
import pl.inpost.recruitmenttask.data.local.entity.ShipmentsDao
import pl.inpost.recruitmenttask.data.remote.api.MockShipmentApi
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentDTO
import pl.inpost.recruitmenttask.domain.data.Shipment
import pl.inpost.recruitmenttask.domain.mapper.Mapper
import pl.inpost.recruitmenttask.domain.repository.ShipmentRepository
import java.io.IOException

class ShipmentRepositoryImpl(
    private val shipmentApi: MockShipmentApi,
    private val shipmentEntityToShipmentMapper: Mapper<ShipmentEntity, Shipment>,
    private val shipmentDtoToShipmentEntityMapper: Mapper<ShipmentDTO, ShipmentEntity>,
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
        //This depends on the requirement. Do we want to shoe anyway the shipments or not? To this we can get from error as well
        val shipments =
            shipmentsDao.getAllShipments().map { shipmentEntityToShipmentMapper.map(it) }
        emit(Response.Success(shipments))

    }.flowOn(defaultDispatcher)

    private suspend fun FlowCollector<Response<List<Shipment>>>.fetchAndRefreshDatabase() {
        try {
            val shipmentDto = if (firstUse) {
                firstUse = false
                emptyList()
            } else {
                shipmentApi.response.shipments
            }
            val shipmentEntity =
                shipmentDto.map {
                    shipmentDtoToShipmentEntityMapper.map(it)
                }
            shipmentsDao.deleteAllShipments()
            shipmentsDao.insertShipments(shipmentEntity)
        } catch (e: IOException) {
            emit(Response.Error<List<Shipment>>(UiText.StringResource(R.string.no_internet)))
            analytics.logError()
        } catch (e: Exception) {
            emit(Response.Error<List<Shipment>>(UiText.StringResource(R.string.unknown_error)))
            analytics.logError()
        }
    }
}