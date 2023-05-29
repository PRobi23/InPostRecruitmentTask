package pl.inpost.recruitmenttask.presentation

import app.cash.turbine.test
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import pl.inpost.recruitmenttask.R
import pl.inpost.recruitmenttask.ShipmentGenerator
import pl.inpost.recruitmenttask.core.util.Response
import pl.inpost.recruitmenttask.core.util.UiText
import pl.inpost.recruitmenttask.domain.data.Shipment
import pl.inpost.recruitmenttask.domain.usecase.ArchiveShipmentUseCase
import pl.inpost.recruitmenttask.domain.usecase.FilterShipmentsUseCase
import pl.inpost.recruitmenttask.domain.usecase.GetShipmentsUseCase
import pl.inpost.recruitmenttask.domain.usecase.GroupShipmentsByOperationHighlightUseCase
import pl.inpost.recruitmenttask.domain.usecase.OrderShipmentsUseCase
import pl.inpost.recruitmenttask.domain.usecase.ShipmentUseCases
import pl.inpost.recruitmenttask.helper.MainDispatcherCoroutinesTestRule
import pl.inpost.recruitmenttask.presentation.shipmentList.ShipmentItem
import pl.inpost.recruitmenttask.presentation.shipmentList.ShipmentListViewModel
import pl.inpost.recruitmenttask.presentation.shipmentList.ShipmentType

@OptIn(ExperimentalCoroutinesApi::class)
class ShipmentListViewModelTest {
    private val getShipmentsUseCase: GetShipmentsUseCase = mockk()
    private val groupShipmentsByOperationHighlightUseCase: GroupShipmentsByOperationHighlightUseCase =
        spyk(GroupShipmentsByOperationHighlightUseCase())
    private val orderShipmentsUseCase: OrderShipmentsUseCase = mockk()
    private val archiveShipmentUseCase: ArchiveShipmentUseCase = mockk()
    private val filterShipmentsUseCase: FilterShipmentsUseCase = mockk()

    @get:Rule
    var coroutinesTestRule = MainDispatcherCoroutinesTestRule()

    private fun createShipmentListViewModel() = ShipmentListViewModel(
        shipmentsUseCases = ShipmentUseCases(
            getShipmentsUseCase = getShipmentsUseCase,
            groupShipmentsByOperationHighlightUseCase = groupShipmentsByOperationHighlightUseCase,
            orderShipmentsUseCase = orderShipmentsUseCase,
            archiveShipmentUseCase = archiveShipmentUseCase,
            filterShipmentsUseCase = filterShipmentsUseCase
        )
    )

    @Test
    fun `when repository getShipmentsUseCase tells that there is a network error the state is set to state`() =
        runTest {
            // given
            coEvery {
                getShipmentsUseCase()
            } returns flowOf(
                Response.Error<List<Shipment>>(error = UiText.StringResource(R.string.no_internet))
            )

            val viewModel = createShipmentListViewModel()

            // when
            viewModel.shipmentListState.test {
                this.awaitItem()
                val state = this.awaitItem()

                // then
                Truth.assertThat(state.error).isEqualTo(UiText.StringResource(R.string.no_internet))
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when repository getShipmentsUseCase returns elements it is set to the state`() =
        runTest {
            // given
            val operationsHighlightFalseShipment = ShipmentGenerator.createShipment(
                operationsHighlight = false
            )
            val operationsHighlightTrueShipment = ShipmentGenerator.createShipment(
                operationsHighlight = true
            )
            val shipments =
                listOf(operationsHighlightFalseShipment, operationsHighlightTrueShipment)
            val groupedShipments = listOf(
                ShipmentItem(
                    shipmentType = ShipmentType.OTHER,
                    shipments = listOf(operationsHighlightFalseShipment)
                ),
                ShipmentItem(
                    shipmentType = ShipmentType.READY_TO_PICK_UP,
                    shipments = listOf(operationsHighlightTrueShipment)
                )
            )
            coEvery {
                getShipmentsUseCase()
            } returns flowOf(
                Response.Success<List<Shipment>>(data = shipments)
            )
            coEvery {
                groupShipmentsByOperationHighlightUseCase(shipments)
            } returns groupedShipments
            coEvery {
                orderShipmentsUseCase(listOf(operationsHighlightFalseShipment))
            } returns listOf(operationsHighlightFalseShipment)

            coEvery {
                orderShipmentsUseCase(listOf(operationsHighlightTrueShipment))
            } returns listOf(operationsHighlightTrueShipment)
            every {
                filterShipmentsUseCase(groupedShipments, FilterShipmentsUseCase.Filter.ALL)
            } returns groupedShipments

            val viewModel = createShipmentListViewModel()

            // when
            viewModel.shipmentListState.test {
                this.awaitItem()
                val state = this.awaitItem()

                // then
                Truth.assertThat(state.shipmentItems).isEqualTo(groupedShipments)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when repository archiveShipment is called returns elements it is set to the state`() =
        runTest {
            // given
            val operationsHighlightFalseShipment = ShipmentGenerator.createShipment(
                operationsHighlight = false
            )
            val operationsHighlightTrueShipment = ShipmentGenerator.createShipment(
                operationsHighlight = true
            )
            val shipments =
                listOf(operationsHighlightTrueShipment)
            val groupedShipments = listOf(
                ShipmentItem(
                    shipmentType = ShipmentType.READY_TO_PICK_UP,
                    shipments = listOf(operationsHighlightTrueShipment)
                )
            )
            coEvery {
                archiveShipmentUseCase(operationsHighlightFalseShipment)
            } returns flowOf(Response.Success(Unit))
            coEvery {
                getShipmentsUseCase()
            } returns flowOf(
                Response.Success<List<Shipment>>(data = shipments)
            )
            coEvery {
                groupShipmentsByOperationHighlightUseCase(shipments)
            } returns groupedShipments
            coEvery {
                orderShipmentsUseCase(listOf(operationsHighlightFalseShipment))
            } returns listOf(operationsHighlightFalseShipment)

            coEvery {
                orderShipmentsUseCase(listOf(operationsHighlightTrueShipment))
            } returns listOf(operationsHighlightTrueShipment)
            every {
                filterShipmentsUseCase(groupedShipments, FilterShipmentsUseCase.Filter.ALL)
            } returns groupedShipments
            val viewModel = createShipmentListViewModel()

            // when
            viewModel.shipmentListState.test {
                this.awaitItem()
                val state = this.awaitItem()

                // then
                Truth.assertThat(state.shipmentItems).isEqualTo(groupedShipments)
                cancelAndIgnoreRemainingEvents()
            }
        }
}