package pl.inpost.recruitmenttask.domain.usecase

import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import pl.inpost.recruitmenttask.ShipmentGenerator
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentTypeDTO
import pl.inpost.recruitmenttask.data.repository.fake.FakeShipmentRepository
import pl.inpost.recruitmenttask.domain.data.Shipment
import pl.inpost.recruitmenttask.domain.data.ShipmentStatus
import pl.inpost.recruitmenttask.domain.repository.ShipmentRepository

@OptIn(ExperimentalCoroutinesApi::class)
class GetShipmentsUseCaseTest {

    private fun createGetShipmentsUseCase(shipmentRepository: ShipmentRepository) =
        GetShipmentsUseCase(shipmentRepository)

    private val mockShipmentsRepository: ShipmentRepository = mockk()
    private val fakeShipmentsRepository = FakeShipmentRepository()

    @Test
    fun `when use is called the shipment repository get shipments are called`() = runTest {
        // given
        val getShipmentsUseCase = createGetShipmentsUseCase(mockShipmentsRepository)
        val createdShipments =
            listOf(ShipmentGenerator.createShipment(), ShipmentGenerator.createShipment())

        coEvery {
            mockShipmentsRepository.getShipments()
        } returns createdShipments

        // when
        val shipments = getShipmentsUseCase()

        // then
        coVerify {
            mockShipmentsRepository.getShipments()
        }
        Truth.assertThat(shipments).isEqualTo(createdShipments)
    }

    @Test
    fun `when use is called with fake shipment repository then the fake values are returned`() =
        runTest {
            // given
            val getShipmentsUseCase = createGetShipmentsUseCase(fakeShipmentsRepository)
            val expectedShipments = listOf(
                Shipment(
                    number = 0L,
                    shipmentType = ShipmentTypeDTO.PARCEL_LOCKER.name,
                    status = ShipmentStatus.DELIVERED,
                    operationsHighlight = true,
                    senderEmail = "name@email.com",
                    pickUpDate = null,
                    storedDate = null,
                    expiryDate = null
                ),
                Shipment(
                    number = 1L,
                    shipmentType = ShipmentTypeDTO.PARCEL_LOCKER.name,
                    status = ShipmentStatus.DELIVERED,
                    operationsHighlight = true,
                    senderEmail = "name@email.com",
                    pickUpDate = null,
                    storedDate = null,
                    expiryDate = null
                ),
                Shipment(
                    number = 2L,
                    shipmentType = ShipmentTypeDTO.PARCEL_LOCKER.name,
                    status = ShipmentStatus.DELIVERED,
                    operationsHighlight = true,
                    senderEmail = "name@email.com",
                    pickUpDate = null,
                    storedDate = null,
                    expiryDate = null
                ),
                Shipment(
                    number = 3L,
                    shipmentType = ShipmentTypeDTO.PARCEL_LOCKER.name,
                    status = ShipmentStatus.DELIVERED,
                    operationsHighlight = true,
                    senderEmail = "name@email.com",
                    pickUpDate = null,
                    storedDate = null,
                    expiryDate = null
                ),
                Shipment(
                    number = 4L,
                    shipmentType = ShipmentTypeDTO.PARCEL_LOCKER.name,
                    status = ShipmentStatus.DELIVERED,
                    operationsHighlight = true,
                    senderEmail = "name@email.com",
                    pickUpDate = null,
                    storedDate = null,
                    expiryDate = null
                ),
                Shipment(
                    number = 5L,
                    shipmentType = ShipmentTypeDTO.PARCEL_LOCKER.name,
                    status = ShipmentStatus.DELIVERED,
                    operationsHighlight = true,
                    senderEmail = "name@email.com",
                    pickUpDate = null,
                    storedDate = null,
                    expiryDate = null
                )
            )
            // when
            val shipments = getShipmentsUseCase()

            // then
            Truth.assertThat(shipments.size).isEqualTo(6)
            Truth.assertThat(shipments).isEqualTo(expectedShipments)
        }

}