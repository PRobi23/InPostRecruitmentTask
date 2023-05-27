package pl.inpost.recruitmenttask.domain.mapper

import com.google.common.truth.Truth
import org.junit.Test
import pl.inpost.recruitmenttask.DateGenerator
import pl.inpost.recruitmenttask.ShipmentGenerator
import pl.inpost.recruitmenttask.core.util.formatZonedDateTime
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentStatus
import pl.inpost.recruitmenttask.data.remote.dto.ShipmentType
import pl.inpost.recruitmenttask.domain.data.DateType
import pl.inpost.recruitmenttask.domain.data.Shipment

class ShipmentDtoToShipmentMapperTest {

    private fun createShipmentDtoToShipmentMapper() = ShipmentDtoToShipmentMapper()

    @Test
    fun `when map is called and the input pickupDate is set then dateToShow property is set by pickUpDate and type is PICK_UP`() {
        // given
        val mapper = createShipmentDtoToShipmentMapper()
        val pickUpDate = DateGenerator.getRandomZonedDateTime()

        val input =
            ShipmentGenerator.mockShipmentNetwork(number = "1", pickUpDate = pickUpDate)
        val expectedShipment = Shipment(
            number = "1",
            shipmentType = ShipmentType.PARCEL_LOCKER.name,
            status = ShipmentStatus.DELIVERED.name,
            senderEmail = "name@email.com",
            operationsHighlight = false,
            dateToShow = pickUpDate.formatZonedDateTime(),
            dateType = DateType.PICK_UP
        )
        // when
        val shipment = mapper.map(input)

        // then
        Truth.assertThat(shipment).isEqualTo(expectedShipment)
    }

    @Test
    fun `when map is called and the input expiryDate is set then dateToShow property is set by expiryDate and type is STORED`() {
        // given
        val mapper = createShipmentDtoToShipmentMapper()
        val expiryDate = DateGenerator.getRandomZonedDateTime()

        val input =
            ShipmentGenerator.mockShipmentNetwork(number = "1", expiryDate = expiryDate)
        val expectedShipment = Shipment(
            number = "1",
            shipmentType = ShipmentType.PARCEL_LOCKER.name,
            status = ShipmentStatus.DELIVERED.name,
            senderEmail = "name@email.com",
            operationsHighlight = false,
            dateToShow = expiryDate.formatZonedDateTime(),
            dateType = DateType.EXPIRY
        )
        // when
        val shipment = mapper.map(input)

        // then
        Truth.assertThat(shipment).isEqualTo(expectedShipment)
    }

    @Test
    fun `when map is called and the input storedDate is set then date to show is set by storedDate and type is STORED`() {
        // given
        val mapper = createShipmentDtoToShipmentMapper()
        val storedDate = DateGenerator.getRandomZonedDateTime()

        val input =
            ShipmentGenerator.mockShipmentNetwork(number = "1", storedDate = storedDate)
        val expectedShipment = Shipment(
            number = "1",
            shipmentType = ShipmentType.PARCEL_LOCKER.name,
            status = ShipmentStatus.DELIVERED.name,
            senderEmail = "name@email.com",
            operationsHighlight = false,
            dateToShow = storedDate.formatZonedDateTime(),
            dateType = DateType.STORED
        )
        // when
        val shipment = mapper.map(input)

        // then
        Truth.assertThat(shipment).isEqualTo(expectedShipment)
    }

    @Test
    fun `when map is called and the input pickUpDate and storedDate is set then date to show is set by pickUpDate and type is PICK_UP`() {
        // given
        val mapper = createShipmentDtoToShipmentMapper()
        val pickUpDate = DateGenerator.getRandomZonedDateTime()
        val storedDate = DateGenerator.getRandomZonedDateTime()

        val input =
            ShipmentGenerator.mockShipmentNetwork(
                number = "1",
                pickUpDate = pickUpDate,
                storedDate = storedDate
            )
        val expectedShipment = Shipment(
            number = "1",
            shipmentType = ShipmentType.PARCEL_LOCKER.name,
            status = ShipmentStatus.DELIVERED.name,
            senderEmail = "name@email.com",
            operationsHighlight = false,
            dateToShow = pickUpDate.formatZonedDateTime(),
            dateType = DateType.PICK_UP
        )
        // when
        val shipment = mapper.map(input)

        // then
        Truth.assertThat(shipment).isEqualTo(expectedShipment)
    }

    @Test
    fun `when map is called and the input pickUpDate and expiryDate is set then date to show is set by pickUpDate and type is PICK_UP`() {
        // given
        val mapper = createShipmentDtoToShipmentMapper()
        val pickUpDate = DateGenerator.getRandomZonedDateTime()
        val expiryDate = DateGenerator.getRandomZonedDateTime()

        val input =
            ShipmentGenerator.mockShipmentNetwork(
                number = "1",
                pickUpDate = pickUpDate,
                expiryDate = expiryDate
            )
        val expectedShipment = Shipment(
            number = "1",
            shipmentType = ShipmentType.PARCEL_LOCKER.name,
            status = ShipmentStatus.DELIVERED.name,
            senderEmail = "name@email.com",
            operationsHighlight = false,
            dateToShow = pickUpDate.formatZonedDateTime(),
            dateType = DateType.PICK_UP
        )
        // when
        val shipment = mapper.map(input)

        // then
        Truth.assertThat(shipment).isEqualTo(expectedShipment)
    }

    @Test
    fun `when map is called and the expireDate and storedDate is set then date to show is set by expiryDate and type is EXPIRY`() {
        // given
        val mapper = createShipmentDtoToShipmentMapper()
        val storedDate = DateGenerator.getRandomZonedDateTime()
        val expiryDate = DateGenerator.getRandomZonedDateTime()

        val input =
            ShipmentGenerator.mockShipmentNetwork(
                number = "1",
                storedDate = storedDate,
                expiryDate = expiryDate
            )
        val expectedShipment = Shipment(
            number = "1",
            shipmentType = ShipmentType.PARCEL_LOCKER.name,
            status = ShipmentStatus.DELIVERED.name,
            senderEmail = "name@email.com",
            operationsHighlight = false,
            dateToShow = expiryDate.formatZonedDateTime(),
            dateType = DateType.EXPIRY
        )
        // when
        val shipment = mapper.map(input)

        // then
        Truth.assertThat(shipment).isEqualTo(expectedShipment)
    }

    @Test
    fun `when map is called and all dates are set set then date to show is set by pickUpDate and type is PICK_UP`() {
        // given
        val mapper = createShipmentDtoToShipmentMapper()
        val pickUpDate = DateGenerator.getRandomZonedDateTime()
        val storedDate = DateGenerator.getRandomZonedDateTime()
        val expiryDate = DateGenerator.getRandomZonedDateTime()

        val input =
            ShipmentGenerator.mockShipmentNetwork(
                number = "1",
                pickUpDate = pickUpDate,
                storedDate = storedDate,
                expiryDate = expiryDate
            )
        val expectedShipment = Shipment(
            number = "1",
            shipmentType = ShipmentType.PARCEL_LOCKER.name,
            status = ShipmentStatus.DELIVERED.name,
            senderEmail = "name@email.com",
            operationsHighlight = false,
            dateToShow = pickUpDate.formatZonedDateTime(),
            dateType = DateType.PICK_UP
        )
        // when
        val shipment = mapper.map(input)

        // then
        Truth.assertThat(shipment).isEqualTo(expectedShipment)
    }

    @Test
    fun `when map is called and the all dates are null then dateToShow property is null and type is NO_DATE`() {
        // given
        val mapper = createShipmentDtoToShipmentMapper()

        val input =
            ShipmentGenerator.mockShipmentNetwork(
                number = "1"
            )
        val expectedShipment = Shipment(
            number = "1",
            shipmentType = ShipmentType.PARCEL_LOCKER.name,
            status = ShipmentStatus.DELIVERED.name,
            senderEmail = "name@email.com",
            operationsHighlight = false,
            dateToShow = null,
            dateType = DateType.NO_DATE
        )
        // when
        val shipment = mapper.map(input)

        // then
        Truth.assertThat(shipment).isEqualTo(expectedShipment)
    }
}