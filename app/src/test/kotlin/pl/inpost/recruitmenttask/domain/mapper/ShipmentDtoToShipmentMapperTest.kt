package pl.inpost.recruitmenttask.domain.mapper

import com.google.common.truth.Truth
import org.junit.Test
import pl.inpost.recruitmenttask.DateGenerator
import pl.inpost.recruitmenttask.ShipmentGenerator
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentStatusDTO
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentTypeDTO
import pl.inpost.recruitmenttask.domain.data.DateType
import pl.inpost.recruitmenttask.domain.data.Shipment
import pl.inpost.recruitmenttask.domain.data.ShipmentStatus

class ShipmentDtoToShipmentMapperTest {

    private fun createShipmentDtoToShipmentMapper() = ShipmentDtoToShipmentMapper()

    @Test
    fun `when map is called then the shipment dto is mapped properly`() {
        // given
        val mapper = createShipmentDtoToShipmentMapper()

        val expiryDate = DateGenerator.getRandomZonedDateTime()
        val storedDate = DateGenerator.getRandomZonedDateTime()
        val pickUpDate = DateGenerator.getRandomZonedDateTime()

        val shipmentDTO =
            ShipmentGenerator.mockShipmentNetwork(
                number = 2L,
                type = ShipmentTypeDTO.COURIER,
                status = ShipmentStatusDTO.CONFIRMED,
                expiryDate = expiryDate,
                storedDate = storedDate,
                pickUpDate = pickUpDate
            )

        val expectedShipment = Shipment(
            number = 2L,
            shipmentType = ShipmentTypeDTO.COURIER.name,
            status = ShipmentStatus.CONFIRMED,
            expiryDate = expiryDate,
            storedDate = storedDate,
            pickUpDate = pickUpDate,
            senderEmail = "name@email.com",
            operationsHighlight = false
        )

        // when
        val shipment = mapper.map(shipmentDTO)

        // then
        Truth.assertThat(shipment).isEqualTo(expectedShipment)
    }

    @Test
    fun `when getDateType is called and the input pickupDate is set then dateToShow property is set by pickUpDate and type is PICK_UP`() {
        // given
        val mapper = createShipmentDtoToShipmentMapper()
        val pickUpDate = DateGenerator.getRandomZonedDateTime()

        // when
        val dateType =
            mapper.getDateType(pickUpDate = pickUpDate, storedDate = null, expiryDate = null)

        // then
        Truth.assertThat(dateType).isEqualTo(DateType.PICK_UP)
    }

    @Test
    fun `when getDateType is called and the input expiryDate is set then dateToShow property is set by expiryDate and type is STORED`() {
        // given
        val mapper = createShipmentDtoToShipmentMapper()
        val expiryDate = DateGenerator.getRandomZonedDateTime()

        // when
        val dateType =
            mapper.getDateType(pickUpDate = null, storedDate = null, expiryDate = expiryDate)

        // then
        Truth.assertThat(dateType).isEqualTo(DateType.EXPIRY)
    }

    @Test
    fun `when getDateType is called and the input storedDate is set then date to show is set by storedDate and type is STORED`() {
        // given
        val mapper = createShipmentDtoToShipmentMapper()
        val storedDate = DateGenerator.getRandomZonedDateTime()

        // when
        val dateType =
            mapper.getDateType(pickUpDate = null, storedDate = storedDate, expiryDate = null)

        // then
        Truth.assertThat(dateType).isEqualTo(DateType.STORED)
    }

    @Test
    fun `when getDateType is called and the input pickUpDate and storedDate is set then date to show is set by pickUpDate and type is PICK_UP`() {
        // given
        val mapper = createShipmentDtoToShipmentMapper()
        val pickUpDate = DateGenerator.getRandomZonedDateTime()
        val storedDate = DateGenerator.getRandomZonedDateTime()

        // when
        val dateType =
            mapper.getDateType(pickUpDate = pickUpDate, storedDate = storedDate, expiryDate = null)

        // then
        Truth.assertThat(dateType).isEqualTo(DateType.PICK_UP)
    }

    @Test
    fun `when getDateType is called and the input pickUpDate and expiryDate is set then date to show is set by pickUpDate and type is PICK_UP`() {
        // given
        val mapper = createShipmentDtoToShipmentMapper()
        val pickUpDate = DateGenerator.getRandomZonedDateTime()
        val expiryDate = DateGenerator.getRandomZonedDateTime()

        // when
        val dateType =
            mapper.getDateType(pickUpDate = pickUpDate, storedDate = null, expiryDate = expiryDate)

        // then
        Truth.assertThat(dateType).isEqualTo(DateType.PICK_UP)
    }

    @Test
    fun `when getDateType is called and the expireDate and storedDate is set then date to show is set by expiryDate and type is EXPIRY`() {
        // given
        val mapper = createShipmentDtoToShipmentMapper()
        val storedDate = DateGenerator.getRandomZonedDateTime()
        val expiryDate = DateGenerator.getRandomZonedDateTime()

        // when
        val dateType =
            mapper.getDateType(pickUpDate = null, storedDate = storedDate, expiryDate = expiryDate)

        // then
        Truth.assertThat(dateType).isEqualTo(DateType.EXPIRY)
    }

    @Test
    fun `when map is called and all dates are set set then date to show is set by pickUpDate and type is PICK_UP`() {
        // given
        val mapper = createShipmentDtoToShipmentMapper()
        val pickUpDate = DateGenerator.getRandomZonedDateTime()
        val storedDate = DateGenerator.getRandomZonedDateTime()
        val expiryDate = DateGenerator.getRandomZonedDateTime()

        // when
        val dateType =
            mapper.getDateType(
                pickUpDate = pickUpDate,
                storedDate = storedDate,
                expiryDate = expiryDate
            )

        // then
        Truth.assertThat(dateType).isEqualTo(DateType.PICK_UP)
    }


    @Test
    fun `when map is called and the all dates are null then dateToShow property is null and type is NO_DATE`() {
        // given
        val mapper = createShipmentDtoToShipmentMapper()

        // when
        val dateType =
            mapper.getDateType(
                pickUpDate = null,
                storedDate = null,
                expiryDate = null
            )

        // then
        Truth.assertThat(dateType).isEqualTo(DateType.NO_DATE)
    }
}