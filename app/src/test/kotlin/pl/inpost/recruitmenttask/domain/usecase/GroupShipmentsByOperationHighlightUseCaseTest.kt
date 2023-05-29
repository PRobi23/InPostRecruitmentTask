package pl.inpost.recruitmenttask.domain.usecase

import com.google.common.truth.Truth
import org.junit.Test
import pl.inpost.recruitmenttask.ShipmentGenerator
import pl.inpost.recruitmenttask.presentation.shipmentList.ShipmentItem
import pl.inpost.recruitmenttask.presentation.shipmentList.ShipmentType

class GroupShipmentsByOperationHighlightUseCaseTest {

    private fun createGroupShipmentsByOperationHighlightUseCase() =
        GroupShipmentsByOperationHighlightUseCase()

    @Test
    fun `when use is called then the passed shipments are grouped by operations highlight `() {
        // given
        val groupShipmentsByOperationHighlightUseCase =
            createGroupShipmentsByOperationHighlightUseCase()
        val operationsHighlightTrue1 = ShipmentGenerator.createShipment(true)
        val operationsHighlightTrue2 = ShipmentGenerator.createShipment(true)

        val operationsHighlightFalse1 = ShipmentGenerator.createShipment(false)
        val operationsHighlightFalse2 = ShipmentGenerator.createShipment(false)

        val shipmentList = listOf(
            operationsHighlightTrue1,
            operationsHighlightTrue2,
            operationsHighlightFalse1,
            operationsHighlightFalse2
        )

        val expectedShipments = listOf(
            ShipmentItem(
                shipmentType = ShipmentType.READY_TO_PICK_UP,
                shipments = listOf(
                    operationsHighlightTrue1,
                    operationsHighlightTrue2
                )
            ),
            ShipmentItem(
                shipmentType = ShipmentType.OTHER,
                shipments = listOf(
                    operationsHighlightFalse1,
                    operationsHighlightFalse2
                )
            )
        )

        // when
        val groupedShipments = groupShipmentsByOperationHighlightUseCase(shipmentList)

        // then
        Truth.assertThat(groupedShipments).isEqualTo(expectedShipments)
    }
}