package pl.inpost.recruitmenttask

import pl.inpost.recruitmenttask.data.remote.dto.CustomerNetworkDTO
import pl.inpost.recruitmenttask.data.remote.dto.EventLogNetworkDTO
import pl.inpost.recruitmenttask.data.remote.dto.OperationsNetworkDTO
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentDTO
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentStatus
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentType
import pl.inpost.recruitmenttask.domain.data.Shipment
import java.time.ZonedDateTime
import kotlin.random.Random

object ShipmentGenerator {

    private val alphabet: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private fun randomWord(): String = List((1..10).random()) { alphabet.random() }.joinToString("")

    fun createShipment(): Shipment =
        Shipment(
            number = randomWord(),
            shipmentType = randomWord(),
            status = randomWord()
        )

    fun mockShipmentNetwork(
        number: String,
        type: ShipmentType = ShipmentType.PARCEL_LOCKER,
        status: ShipmentStatus = ShipmentStatus.DELIVERED,
        sender: CustomerNetworkDTO? = mockCustomerNetwork(),
        receiver: CustomerNetworkDTO? = mockCustomerNetwork(),
        operations: OperationsNetworkDTO = mockOperationsNetwork(),
        eventLog: List<EventLogNetworkDTO> = emptyList(),
        openCode: String? = null,
        expireDate: ZonedDateTime? = null,
        storedDate: ZonedDateTime? = null,
        pickupDate: ZonedDateTime? = null
    ) = ShipmentDTO(
        number = number,
        shipmentType = type.name,
        status = status.name,
        eventLog = eventLog,
        openCode = openCode,
        expiryDate = expireDate,
        storedDate = storedDate,
        pickUpDate = pickupDate,
        receiver = receiver,
        sender = sender,
        operations = operations
    )

    private fun mockCustomerNetwork(
        email: String = "name@email.com",
        phoneNumber: String = "123 123 123",
        name: String = "Jan Kowalski"
    ) = CustomerNetworkDTO(
        email = email,
        phoneNumber = phoneNumber,
        name = name
    )

    private fun mockOperationsNetwork(
        manualArchive: Boolean = false,
        delete: Boolean = false,
        collect: Boolean = false,
        highlight: Boolean = false,
        expandAvizo: Boolean = false,
        endOfWeekCollection: Boolean = false
    ) = OperationsNetworkDTO(
        manualArchive = manualArchive,
        delete = delete,
        collect = collect,
        highlight = highlight,
        expandAvizo = expandAvizo,
        endOfWeekCollection = endOfWeekCollection
    )

}