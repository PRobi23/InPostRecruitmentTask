package pl.inpost.recruitmenttask.domain.mapper

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import pl.inpost.recruitmenttask.DateGenerator
import pl.inpost.recruitmenttask.ShipmentGenerator
import pl.inpost.recruitmenttask.core.dateTime.DateTimeUtils
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentStatusDTO
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentTypeDTO
import pl.inpost.recruitmenttask.domain.data.DateType
import pl.inpost.recruitmenttask.domain.data.Shipment
import pl.inpost.recruitmenttask.domain.data.ShipmentStatus

class ShipmentEntityToShipmentMapperTest {

    private val dateTimeUtils: DateTimeUtils = mockk()
    private fun createShipmentDtoToShipmentMapper() = ShipmentEntityToShipmentMapper(dateTimeUtils)

    @Test
    fun `when map is called then the shipment dto is mapped properly`() {
        // given
        val mapper = createShipmentDtoToShipmentMapper()

        val expiryDate = DateGenerator.getRandomZonedDateTime()
        val storedDate = DateGenerator.getRandomZonedDateTime()
        val pickUpDate = DateGenerator.getRandomZonedDateTime()
        every {
            dateTimeUtils.epochMillisecondsToZonedDateTime(expiryDate.toInstant().toEpochMilli())
        } returns expiryDate
        every {
            dateTimeUtils.epochMillisecondsToZonedDateTime(storedDate.toInstant().toEpochMilli())
        } returns storedDate
        every {
            dateTimeUtils.epochMillisecondsToZonedDateTime(pickUpDate.toInstant().toEpochMilli())
        } returns pickUpDate

        val shipmentEntity =
            ShipmentGenerator.createShipmentEntity(
                number = "2",
                operationsHighlight = false,
                shipmentType = ShipmentTypeDTO.COURIER.name,
                status = ShipmentStatusDTO.CONFIRMED.name,
                senderEmail = "name@email.com",
                expiryDate = expiryDate.toInstant().toEpochMilli(),
                storedDate = storedDate.toInstant().toEpochMilli(),
                pickUpDate = pickUpDate.toInstant().toEpochMilli()
            )

        val expectedShipment = Shipment(
            number = "2",
            shipmentType = ShipmentTypeDTO.COURIER.name,
            status = ShipmentStatus.CONFIRMED,
            expiryDate = expiryDate,
            storedDate = storedDate,
            pickUpDate = pickUpDate,
            senderEmail = "name@email.com",
            operationsHighlight = false
        )

        // when
        val shipment = mapper.map(shipmentEntity)

        // then
        Truth.assertThat(shipment).isEqualTo(expectedShipment)
    }
}