package pl.inpost.recruitmenttask.presentation.shipmentList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import pl.inpost.recruitmenttask.core.util.Response
import pl.inpost.recruitmenttask.domain.data.Shipment
import pl.inpost.recruitmenttask.domain.usecase.GetShipmentsUseCase
import pl.inpost.recruitmenttask.domain.usecase.GroupShipmentsByOperationHighlightUseCase
import pl.inpost.recruitmenttask.domain.usecase.OrderShipmentsUseCase
import pl.inpost.recruitmenttask.domain.usecase.ShipmentUseCases
import javax.inject.Inject

@HiltViewModel
class ShipmentListViewModel @Inject constructor(
    private val shipmentsUseCases: ShipmentUseCases
) : ViewModel() {

    private val _shipmentListState = MutableStateFlow<ShipmentListState>(ShipmentListState())
    val shipmentListState: StateFlow<ShipmentListState> = _shipmentListState

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            shipmentsUseCases.getShipmentsUseCase().onEach { result ->
                setStateByResponseResult(result)
            }.launchIn(viewModelScope)
        }
    }

    fun archiveShipment(shipment: Shipment) {
        viewModelScope.launch {
            shipmentsUseCases.archiveShipmentUseCase(shipment).onEach { result ->
                when (result) {
                    is Response.Error -> {
                        _shipmentListState.value =
                            ShipmentListState(isLoading = false, error = result.error)
                    }

                    is Response.Loading -> {
                        _shipmentListState.value =
                            ShipmentListState(isLoading = true)
                    }

                    is Response.Success -> {
                        refreshData()
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun setStateByResponseResult(result: Response<List<Shipment>>) {
        when (result) {
            is Response.Success -> {
                val shipments = result.data ?: emptyList()
                val groupedShipments =
                    shipmentsUseCases.groupShipmentsByOperationHighlightUseCase(shipments)
                groupedShipments.forEach {
                    shipmentsUseCases.orderShipmentsUseCase(it.shipments)
                }
                _shipmentListState.value =
                    ShipmentListState(isLoading = false, shipmentItems = groupedShipments)
            }

            is Response.Error -> {
                _shipmentListState.value = ShipmentListState(
                    error = result.error,
                    isLoading = false
                )
            }

            is Response.Loading -> {
                _shipmentListState.value = ShipmentListState(isLoading = true)
            }
        }
    }
}
