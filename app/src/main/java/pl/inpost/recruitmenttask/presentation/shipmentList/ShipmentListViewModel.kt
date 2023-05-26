package pl.inpost.recruitmenttask.presentation.shipmentList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.inpost.recruitmenttask.domain.repository.ShipmentRepository
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentDTO
import pl.inpost.recruitmenttask.core.util.setState
import pl.inpost.recruitmenttask.domain.data.Shipment
import pl.inpost.recruitmenttask.domain.usecase.GetShipmentsUseCase
import javax.inject.Inject

@HiltViewModel
class ShipmentListViewModel @Inject constructor(
    private val getShipmentsUseCase: GetShipmentsUseCase
) : ViewModel() {

    private val mutableViewState = MutableLiveData<List<Shipment>>(emptyList())
    val viewState: LiveData<List<Shipment>> = mutableViewState

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            val shipments = getShipmentsUseCase()
            mutableViewState.setState { shipments }
        }
    }
}
