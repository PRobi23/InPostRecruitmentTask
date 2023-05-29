package pl.inpost.recruitmenttask.domain.usecase

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import pl.inpost.recruitmenttask.ShipmentGenerator
import pl.inpost.recruitmenttask.domain.data.ShipmentComparator

class OrderShipmentsUseCaseTest {
    private val comparator: ShipmentComparator = mockk()
    private fun createOrderShipmentsUseCase() = OrderShipmentsUseCase(comparator)

    @Test
    fun `when use case is executed compare is called with the comparator`() {
        // given
        val useCase = createOrderShipmentsUseCase()
        val firstShipment = ShipmentGenerator.createShipment()
        val secondShipment = ShipmentGenerator.createShipment()

        val shipments = listOf(
            firstShipment,
            secondShipment
        )
        every { comparator.compare(any(), any()) } returns 1

        // when
        val orderedList = useCase(shipments)

        // then
        Truth.assertThat(orderedList).isEqualTo(listOf(firstShipment, secondShipment))
    }
}