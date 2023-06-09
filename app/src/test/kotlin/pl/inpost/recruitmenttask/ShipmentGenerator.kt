package pl.inpost.recruitmenttask

import pl.inpost.recruitmenttask.data.local.entity.ShipmentEntity
import pl.inpost.recruitmenttask.data.remote.dto.CustomerNetworkDTO
import pl.inpost.recruitmenttask.data.remote.dto.EventLogNetworkDTO
import pl.inpost.recruitmenttask.data.remote.dto.OperationsNetworkDTO
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentDTO
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentStatusDTO
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentTypeDTO
import pl.inpost.recruitmenttask.domain.data.Shipment
import pl.inpost.recruitmenttask.domain.data.ShipmentStatus
import pl.inpost.recruitmenttask.presentation.shipmentList.ShipmentType
import java.time.ZonedDateTime
import kotlin.random.Random.Default.nextInt
import kotlin.random.Random.Default.nextLong

object ShipmentGenerator {

    private val alphabet: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private fun randomWord(): String = List((1..10).random()) { alphabet.random() }.joinToString("")

    fun createShipment(
        operationsHighlight: Boolean = true,
        status: ShipmentStatus = ShipmentStatus.values()[nextInt(ShipmentStatus.values().size - 1)],
        pickUpDate: ZonedDateTime? = DateGenerator.getRandomZonedDateTime(),
        expiryDate: ZonedDateTime? = DateGenerator.getRandomZonedDateTime(),
        storedDate: ZonedDateTime? = DateGenerator.getRandomZonedDateTime(),
        number: String = nextLong().toString()
    ): Shipment =
        Shipment(
            number = number,
            shipmentType = randomWord(),
            status = status,
            operationsHighlight = operationsHighlight,
            senderEmail = randomWord() + "@.gmail.com",
            expiryDate = expiryDate,
            storedDate = storedDate,
            pickUpDate = pickUpDate
        )

    fun createShipmentEntity(
        operationsHighlight: Boolean = true,
        shipmentType: String = randomWord(),
        status: String = ShipmentStatus.values()[nextInt(ShipmentStatus.values().size - 1)].name,
        senderEmail: String = randomWord() + "@.gmail.com",
        pickUpDate: Long? = DateGenerator.getRandomZonedDateTime().toInstant().toEpochMilli(),
        expiryDate: Long? = DateGenerator.getRandomZonedDateTime().toInstant().toEpochMilli(),
        storedDate: Long? = DateGenerator.getRandomZonedDateTime().toInstant().toEpochMilli(),
        number: String = nextLong().toString()
    ): ShipmentEntity =
        ShipmentEntity(
            number = number,
            shipmentType = shipmentType,
            status = status,
            operationsHighlight = operationsHighlight,
            senderEmail = senderEmail,
            expiryDate = expiryDate,
            storedDate = storedDate,
            pickUpDate = pickUpDate
        )

    fun createShipmentFromNetwork(
        number: Long,
        type: ShipmentTypeDTO = ShipmentTypeDTO.PARCEL_LOCKER,
        status: ShipmentStatusDTO = ShipmentStatusDTO.DELIVERED,
        sender: CustomerNetworkDTO? = mockCustomerNetwork(),
        receiver: CustomerNetworkDTO? = mockCustomerNetwork(),
        operations: OperationsNetworkDTO = mockOperationsNetwork(),
        eventLog: List<EventLogNetworkDTO> = emptyList(),
        openCode: String? = null,
        expiryDate: ZonedDateTime? = null,
        storedDate: ZonedDateTime? = null,
        pickUpDate: ZonedDateTime? = null
    ) = ShipmentDTO(
        number = number.toString(),
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