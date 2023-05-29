package pl.inpost.recruitmenttask.domain.data.repository

import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import pl.inpost.recruitmenttask.core.analytics.Analytics
import pl.inpost.recruitmenttask.core.dateTime.DateTimeUtils
import pl.inpost.recruitmenttask.core.dateTime.DateTimeUtilsImpl
import pl.inpost.recruitmenttask.core.util.Response
import pl.inpost.recruitmenttask.data.local.entity.ShipmentEntity
import pl.inpost.recruitmenttask.data.local.entity.ShipmentsDao
import pl.inpost.recruitmenttask.data.remote.api.MockShipmentApi
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentDTO
import pl.inpost.recruitmenttask.data.repository.ShipmentRepositoryImpl
import pl.inpost.recruitmenttask.domain.data.Shipment
import pl.inpost.recruitmenttask.domain.mapper.Mapper
import pl.inpost.recruitmenttask.domain.mapper.ShipmentDtoToShipmentEntityMapper
import pl.inpost.recruitmenttask.domain.mapper.ShipmentEntityToShipmentMapper

@OptIn(ExperimentalCoroutinesApi::class)
class ShipmentRepositoryImplTest {

    private val shipmentApi: MockShipmentApi = mockk()
    private val analytics: Analytics = mockk()
    private val shipmentsDao: ShipmentsDao = mockk()
    private val dateTimeUtils: DateTimeUtils = spyk(DateTimeUtilsImpl())
    private val shipmentEntityToShipmentMapper: Mapper<ShipmentEntity, Shipment> =
        spyk(ShipmentEntityToShipmentMapper(dateTimeUtils))
    private val shipmentDtoToShipmentEntityMapper: Mapper<ShipmentDTO, ShipmentEntity> =
        spyk(ShipmentDtoToShipmentEntityMapper(dateTimeUtils))

    private fun createShipmentRepository(dispatcher: CoroutineDispatcher) =
        ShipmentRepositoryImpl(
            shipmentApi = shipmentApi,
            analytics = analytics,
            defaultDispatcher = dispatcher,
            shipmentsDao = shipmentsDao,
            shipmentEntityToShipmentMapper = shipmentEntityToShipmentMapper,
            shipmentDtoToShipmentEntityMapper = shipmentDtoToShipmentEntityMapper
        )

    @Test
    fun `the initial state is loading when calling get dog shipments`() = runTest {
        // given
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val repository = createShipmentRepository(testDispatcher)

        coEvery {
            shipmentApi.response.shipments
        } returns emptyList()
        coEvery {
            shipmentsDao.deleteAllShipments()
        } returns Unit
        coEvery {
            shipmentsDao.insertShipments(emptyList())
        } returns Unit

        // when
        val shipmentsResponse = repository.getShipments()
        val firstItem = shipmentsResponse.first()

        // then
        Truth.assertThat(firstItem).isInstanceOf(Response.Loading::class.java)
    }

    @Test
    fun `the first call returns an empty list of shipments`() = runTest {
        // given
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val repository = createShipmentRepository(testDispatcher)

        coEvery {
            shipmentApi.response.shipments
        } returns emptyList()
        coEvery {
            shipmentsDao.deleteAllShipments()
        } returns Unit
        coEvery {
            shipmentsDao.insertShipments(emptyList())
        } returns Unit
        coEvery {
            shipmentsDao.getAllShipments()
        } returns emptyList()

        // when
        val shipmentsResponse = repository.getShipments().toList()
        val firstItem = shipmentsResponse.first()
        val shipments = shipmentsResponse[1]

        // then
        Truth.assertThat(firstItem).isInstanceOf(Response.Loading::class.java)
        coVerify {
            shipmentsDao.insertShipments(emptyList())
        }
        Truth.assertThat(shipments.data!!.isEmpty()).isTrue()
    }
}