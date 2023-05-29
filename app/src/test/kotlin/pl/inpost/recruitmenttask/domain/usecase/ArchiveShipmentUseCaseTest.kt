package pl.inpost.recruitmenttask.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import pl.inpost.recruitmenttask.ShipmentGenerator
import pl.inpost.recruitmenttask.core.util.Response
import pl.inpost.recruitmenttask.domain.repository.ShipmentRepository

class ArchiveShipmentUseCaseTest {

    private val shipmentRepository: ShipmentRepository = mockk()
    private fun createArchiveShipmentUseCase() = ArchiveShipmentUseCase(shipmentRepository)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when use case is called then repository archiveShipment function is called`() = runTest {
        // given
        val useCase = createArchiveShipmentUseCase()
        val shipment = ShipmentGenerator.createShipment()
        coEvery {
            shipmentRepository.archiveShipment(shipment)
        } returns flowOf(Response.Success(Unit))

        // when
        useCase(shipment)

        // then
        coVerify {
            shipmentRepository.archiveShipment(shipment)
        }
    }
}