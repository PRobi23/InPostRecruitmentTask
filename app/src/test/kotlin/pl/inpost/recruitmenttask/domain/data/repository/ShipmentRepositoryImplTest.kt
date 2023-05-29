package pl.inpost.recruitmenttask.domain.data.repository

import android.database.sqlite.SQLiteReadOnlyDatabaseException
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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
import pl.inpost.recruitmenttask.R
import pl.inpost.recruitmenttask.ShipmentGenerator
import pl.inpost.recruitmenttask.core.analytics.Analytics
import pl.inpost.recruitmenttask.core.dateTime.DateTimeUtils
import pl.inpost.recruitmenttask.core.dateTime.DateTimeUtilsImpl
import pl.inpost.recruitmenttask.core.util.Response
import pl.inpost.recruitmenttask.core.util.UiText
import pl.inpost.recruitmenttask.data.local.dao.ShipmentsDao
import pl.inpost.recruitmenttask.data.local.entity.ShipmentEntity
import pl.inpost.recruitmenttask.data.remote.api.MockShipmentApi
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentDTO
import pl.inpost.recruitmenttask.data.repository.ShipmentRepositoryImpl
import pl.inpost.recruitmenttask.domain.data.Shipment
import pl.inpost.recruitmenttask.domain.mapper.Mapper
import pl.inpost.recruitmenttask.domain.mapper.ShipmentDtoToShipmentEntityMapper
import pl.inpost.recruitmenttask.domain.mapper.ShipmentEntityToShipmentMapper
import pl.inpost.recruitmenttask.domain.mapper.ShipmentMappers
import pl.inpost.recruitmenttask.domain.mapper.ShipmentToShipmentEntityMapper
import java.io.IOException

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
    private val shipmentToShipmentEntityMapper: Mapper<Shipment, ShipmentEntity> =
        spyk(ShipmentToShipmentEntityMapper(dateTimeUtils))

    private fun createShipmentRepository(dispatcher: CoroutineDispatcher) =
        ShipmentRepositoryImpl(
            shipmentApi = shipmentApi,
            analytics = analytics,
            defaultDispatcher = dispatcher,
            shipmentsDao = shipmentsDao,
            shipmentMappers = ShipmentMappers(
                shipmentEntityToShipmentMapper = shipmentEntityToShipmentMapper,
                shipmentDtoToShipmentEntityMapper = shipmentDtoToShipmentEntityMapper,
                shipmentToShipmentEntityMapper = shipmentToShipmentEntityMapper
            )
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
            shipmentsDao.deleteAllActiveShipments()
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
            shipmentsDao.deleteAllActiveShipments()
        } returns Unit
        coEvery {
            shipmentsDao.insertShipments(emptyList())
        } returns Unit
        coEvery {
            shipmentsDao.getAllActiveShipments()
        } returns emptyList()
        coEvery {
            shipmentsDao.getAllArchivedShipments()
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

    @Test
    fun `if the second call fails with io exception emit no internet error`() = runTest {
        // given
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val repository = createShipmentRepository(testDispatcher)

        coEvery {
            shipmentsDao.deleteAllActiveShipments()
        } returns Unit
        coEvery {
            shipmentsDao.insertShipments(emptyList())
        } returns Unit
        coEvery {
            shipmentsDao.getAllActiveShipments()
        } returns emptyList()
        coEvery {
            shipmentsDao.getAllArchivedShipments()
        } returns emptyList()
        coEvery {
            shipmentsDao.getAllArchivedShipments()
        } returns emptyList()

        repository.getShipments().toList() // first call
        coEvery {
            shipmentApi.response.shipments
        } throws IOException()
        every {
            analytics.logError()
        } returns Unit

        // when
        val shipmentsResponse2 = repository.getShipments().toList()
        val firstItem = shipmentsResponse2.first()
        val error = shipmentsResponse2[1]

        // then
        Truth.assertThat(firstItem).isInstanceOf(Response.Loading::class.java)
        Truth.assertThat(error).isInstanceOf(Response.Error::class.java)

        coVerify {
            shipmentsDao.insertShipments(emptyList())
        }
        verify {
            analytics.logError()
        }
        Truth.assertThat(error.error).isEqualTo(UiText.StringResource(R.string.no_internet))
    }

    @Test
    fun `if the second call fails with exception emit unknown internet error`() = runTest {
        // given
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val repository = createShipmentRepository(testDispatcher)

        coEvery {
            shipmentsDao.deleteAllActiveShipments()
        } returns Unit
        coEvery {
            shipmentsDao.insertShipments(emptyList())
        } returns Unit
        coEvery {
            shipmentsDao.getAllActiveShipments()
        } returns emptyList()
        coEvery {
            shipmentsDao.getAllArchivedShipments()
        } returns emptyList()

        repository.getShipments().toList() // first call
        coEvery {
            shipmentApi.response.shipments
        } throws SQLiteReadOnlyDatabaseException()
        every {
            analytics.logError()
        } returns Unit

        // when
        val shipmentsResponse2 = repository.getShipments().toList()
        val firstItem = shipmentsResponse2.first()
        val error = shipmentsResponse2[1]

        // then
        Truth.assertThat(firstItem).isInstanceOf(Response.Loading::class.java)
        Truth.assertThat(error).isInstanceOf(Response.Error::class.java)

        coVerify {
            shipmentsDao.insertShipments(emptyList())
        }
        verify {
            analytics.logError()
        }
        Truth.assertThat(error.error)
            .isEqualTo(UiText.StringResource(R.string.unknown_error))
    }

    @Test
    fun `if there  is no fail then there is no error`() = runTest {
        // given
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val repository = createShipmentRepository(testDispatcher)
        val shipmentsFromNetwork = listOf(
            ShipmentGenerator.createShipmentFromNetwork(number = 1L),
            ShipmentGenerator.createShipmentFromNetwork(number = 2L)
        )

        coEvery {
            shipmentsDao.deleteAllActiveShipments()
        } returns Unit
        coEvery {
            shipmentsDao.getAllActiveShipments()
        } returns emptyList()
        coEvery {
            shipmentsDao.insertShipments(emptyList())
        } returns Unit
        coEvery {
            shipmentsDao.getAllArchivedShipments()
        } returns emptyList()
        repository.getShipments().toList() // first call

        coEvery {
            shipmentApi.response.shipments
        } returns shipmentsFromNetwork

        // when
        val shipmentsToInsert = shipmentsFromNetwork.map { shipmentDto ->
            ShipmentEntity(
                number = shipmentDto.number,
                shipmentType = shipmentDto.shipmentType,
                status = shipmentDto.status,
                operationsHighlight = shipmentDto.operations.highlight,
                senderEmail = shipmentDto.sender?.email,
                expiryDate = shipmentDto.expiryDate?.let {
                    dateTimeUtils.zonedDateTimeToEpochMilliseconds(
                        it
                    )
                },
                storedDate = shipmentDto.storedDate?.let {
                    dateTimeUtils.zonedDateTimeToEpochMilliseconds(
                        it
                    )
                },
                pickUpDate = shipmentDto.pickUpDate?.let {
                    dateTimeUtils.zonedDateTimeToEpochMilliseconds(
                        it
                    )
                }
            )
        }
        coEvery {
            shipmentsDao.insertShipments(shipmentsToInsert)
        } returns Unit
        val shipmentsResponse2 = repository.getShipments().toList()
        val firstItem = shipmentsResponse2.first()
        val success = shipmentsResponse2[1]

        // then
        Truth.assertThat(firstItem).isInstanceOf(Response.Loading::class.java)
        Truth.assertThat(success).isInstanceOf(Response.Success::class.java)

        coVerify(exactly = 1) {
            shipmentsDao.insertShipments(shipmentsToInsert)
        }
        verify(exactly = 0) {
            analytics.logError()
        }
    }

    @Test
    fun `already archived shipments are not inserted to database`() = runTest {
        // given
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val repository = createShipmentRepository(testDispatcher)
        val firstShipment = ShipmentGenerator.createShipmentFromNetwork(number = 1L)
        val secondShipment = ShipmentGenerator.createShipmentFromNetwork(number = 2L)

        val shipmentsFromNetwork = listOf(
            firstShipment, secondShipment
        )

        coEvery {
            shipmentsDao.deleteAllActiveShipments()
        } returns Unit
        coEvery {
            shipmentsDao.getAllActiveShipments()
        } returns emptyList()
        coEvery {
            shipmentsDao.insertShipments(emptyList())
        } returns Unit
        coEvery {
            shipmentsDao.getAllArchivedShipments()
        } returns emptyList()
        repository.getShipments().toList() // first call

        coEvery {
            shipmentApi.response.shipments
        } returns shipmentsFromNetwork

        coEvery {
            shipmentsDao.getAllArchivedShipments()
        } returns listOf(shipmentDtoToShipmentEntityMapper.map(firstShipment))

        // when
        val shipmentsToInsert = listOf(
            ShipmentEntity(
                number = secondShipment.number,
                shipmentType = secondShipment.shipmentType,
                status = secondShipment.status,
                operationsHighlight = secondShipment.operations.highlight,
                senderEmail = secondShipment.sender?.email,
                expiryDate = secondShipment.expiryDate?.let {
                    dateTimeUtils.zonedDateTimeToEpochMilliseconds(
                        it
                    )
                },
                storedDate = secondShipment.storedDate?.let {
                    dateTimeUtils.zonedDateTimeToEpochMilliseconds(
                        it
                    )
                },
                pickUpDate = secondShipment.pickUpDate?.let {
                    dateTimeUtils.zonedDateTimeToEpochMilliseconds(
                        it
                    )
                }
            ))
        coEvery {
            shipmentsDao.insertShipments(shipmentsToInsert)
        } returns Unit
        val shipmentsResponse2 = repository.getShipments().toList()
        val firstItem = shipmentsResponse2.first()
        val success = shipmentsResponse2[1]

        // then
        Truth.assertThat(firstItem).isInstanceOf(Response.Loading::class.java)
        Truth.assertThat(success).isInstanceOf(Response.Success::class.java)

        coVerify(exactly = 1) {
            shipmentsDao.insertShipments(shipmentsToInsert)
        }
        verify(exactly = 0) {
            analytics.logError()
        }
    }

    @Test
    fun `when archive shipments is called then update is called with archived set to true`() =
        runTest {
            // given
            val testDispatcher = UnconfinedTestDispatcher(testScheduler)
            val repository = createShipmentRepository(testDispatcher)
            val shipment = ShipmentGenerator.createShipment()
            val shipmentEntity = shipmentToShipmentEntityMapper.map(shipment)
            val updatedShipmentEntity = shipmentEntity.copy(archived = true)
            coEvery {
                shipmentsDao.getShipmentByNumber(shipment.number)
            } returns shipmentEntity
            coEvery {
                shipmentsDao.update(updatedShipmentEntity)
            } returns Unit

            // when
            val shipmentResponse = repository.archiveShipment(shipment).toList()
            val firstItem = shipmentResponse.first()
            val success = shipmentResponse[1]

            // then
            coVerify(exactly = 1) {
                shipmentsDao.update(updatedShipmentEntity)
            }
            Truth.assertThat(firstItem).isInstanceOf(Response.Loading::class.java)
            Truth.assertThat(success).isInstanceOf(Response.Success::class.java)
        }

    @Test
    fun `when archive shipments is called and getShipmentByNumber fails error is propagated`() =
        runTest {
            // given
            val testDispatcher = UnconfinedTestDispatcher(testScheduler)
            val repository = createShipmentRepository(testDispatcher)
            val shipment = ShipmentGenerator.createShipment()

            coEvery {
                shipmentsDao.getShipmentByNumber(shipment.number)
            } throws SQLiteReadOnlyDatabaseException()
            every {
                analytics.logError()
            } returns Unit

            // when
            val shipmentResponse = repository.archiveShipment(shipment).toList()
            val firstItem = shipmentResponse.first()
            val error = shipmentResponse[1]

            // then
            Truth.assertThat(firstItem).isInstanceOf(Response.Loading::class.java)
            Truth.assertThat(error).isInstanceOf(Response.Error::class.java)

            Truth.assertThat(error.error)
                .isEqualTo(UiText.StringResource(R.string.unknown_error))
        }
}