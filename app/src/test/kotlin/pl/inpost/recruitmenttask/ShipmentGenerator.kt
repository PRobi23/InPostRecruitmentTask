package pl.inpost.recruitmenttask

import pl.inpost.recruitmenttask.core.util.formatZonedDateTime
import pl.inpost.recruitmenttask.data.remote.dto.CustomerNetworkDTO
import pl.inpost.recruitmenttask.data.remote.dto.EventLogNetworkDTO
import pl.inpost.recruitmenttask.data.remote.dto.OperationsNetworkDTO
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentDTO
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentStatus
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentType
import pl.inpost.recruitmenttask.domain.data.DateType
import pl.inpost.recruitmenttask.domain.data.Shipment
import java.time.ZonedDateTime
import kotlin.random.Random.Default.nextInt

object ShipmentGenerator {

    private val alphabet: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private fun randomWord(): String = List((1..10).random()) { alphabet.random() }.joinToString("")

    fun createShipment(operationsHighlight: Boolean = true): Shipment =
        Shipment(
            number = randomWord(),
            shipmentType = randomWord(),
            status = randomWord(),
            operationsHighlight = operationsHighlight,
            senderEmail = randomWord() + "@.gmail.com",
            dateToShow = DateGenerator.getRandomZonedDateTime().formatZonedDateTime(),
            dateType = DateType.values()[nextInt(3)]
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
        expiryDate: ZonedDateTime? = null,
        storedDate: ZonedDateTime? = null,
        pickUpDate: ZonedDateTime? = null
    ) = ShipmentDTO(
        number = number,
        shipmentType = type.name,
        status = status.name,
        eventLog = eventLog,
        openCode = openCode,
        expiryDate = expiryDate,
        storedDate = storedDate,
        pickUpDate = pickUpDate,
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