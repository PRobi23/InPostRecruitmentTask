package pl.inpost.recruitmenttask.domain.usecase

import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import pl.inpost.recruitmenttask.ShipmentGenerator
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentStatus
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentType
import pl.inpost.recruitmenttask.data.repository.fake.FakeShipmentRepository
import pl.inpost.recruitmenttask.domain.data.Shipment
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
                    number = "0",
                    shipmentType = ShipmentType.PARCEL_LOCKER.name,
                    status = ShipmentStatus.DELIVERED.name,
                    operationsHighlight = true
                ),
                Shipment(
                    number = "1",
                    shipmentType = ShipmentType.PARCEL_LOCKER.name,
                    status = ShipmentStatus.DELIVERED.name,
                    operationsHighlight = true
                ),
                Shipment(
                    number = "2",
                    shipmentType = ShipmentType.PARCEL_LOCKER.name,
                    status = ShipmentStatus.DELIVERED.name,
                    operationsHighlight = true
                ),
                Shipment(
                    number = "3",
                    shipmentType = ShipmentType.PARCEL_LOCKER.name,
                    status = ShipmentStatus.DELIVERED.name,
                    operationsHighlight = true
                ),
                Shipment(
                    number = "4",
                    shipmentType = ShipmentType.PARCEL_LOCKER.name,
                    status = ShipmentStatus.DELIVERED.name,
                    operationsHighlight = true
                ),
                Shipment(
                    number = "5",
                    shipmentType = ShipmentType.PARCEL_LOCKER.name,
                    status = ShipmentStatus.DELIVERED.name,
                    operationsHighlight = true
                )
            )
            // when
            val shipments = getShipmentsUseCase()

            // then
            Truth.assertThat(shipments.size).isEqualTo(6)
            Truth.assertThat(shipments).isEqualTo(expectedShipments)
        }

}