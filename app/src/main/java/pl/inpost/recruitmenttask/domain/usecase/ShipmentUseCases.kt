package pl.inpost.recruitmenttask.domain.usecase

import javax.inject.Inject

data class ShipmentUseCases @Inject constructor(
    val getShipmentsUseCase: GetShipmentsUseCase,
    val groupShipmentsByOperationHighlightUseCase: GroupShipmentsByOperationHighlightUseCase,
    val orderShipmentsUseCase: OrderShipmentsUseCase,
    val archiveShipmentUseCase: ArchiveShipmentUseCase
)