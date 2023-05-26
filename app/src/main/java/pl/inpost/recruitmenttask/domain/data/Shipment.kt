package pl.inpost.recruitmenttask.domain.data

data class Shipment(
    val number: String,
    val shipmentType: String,
    val status: String,
    val operationsHighlight: Boolean
)