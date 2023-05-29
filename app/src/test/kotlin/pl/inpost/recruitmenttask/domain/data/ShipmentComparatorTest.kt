package pl.inpost.recruitmenttask.domain.data

import com.google.common.truth.Truth
import org.junit.Test
import pl.inpost.recruitmenttask.DateGenerator
import pl.inpost.recruitmenttask.ShipmentGenerator

class ShipmentComparatorTest {

    private fun createComparator() = ShipmentComparator()

    @Test
    fun `when first shipment has ADOPTED_AT_SORTING_CENTER status and second shipment has SENT_FROM_SOURCE_BRANCH status returned value is negative `() {
        // given
        val firstShipment = ShipmentGenerator.createShipment(
            status = ShipmentStatus.ADOPTED_AT_SORTING_CENTER
        )
        val secondShipment = ShipmentGenerator.createShipment(
            status = ShipmentStatus.SENT_FROM_SOURCE_BRANCH
        )
        val comparator = createComparator()

        // when
        val order = comparator.compare(firstShipment, secondShipment)

        // then
        Truth.assertThat(order).isLessThan(0)
    }

    @Test
    fun `when first shipment has AVIZO status and second shipment has SENT_FROM_SOURCE_BRANCH returned value is positive `() {
        // given
        val firstShipment = ShipmentGenerator.createShipment(
            status = ShipmentStatus.AVIZO
        )
        val secondShipment = ShipmentGenerator.createShipment(
            status = ShipmentStatus.SENT_FROM_SOURCE_BRANCH
        )
        val comparator = createComparator()

        // when
        val order = comparator.compare(firstShipment, secondShipment)

        // then
        Truth.assertThat(order).isAtLeast(1)
    }

    @Test
    fun `when compare is called and status is the same and first pick up date is more then second one then first is bigger`() {
        // given
        val randomZonedDateTime = DateGenerator.MOCK_ZONED_DATE_TIME
        val firstShipment = ShipmentGenerator.createShipment(
            status = ShipmentStatus.AVIZO,
            pickUpDate = randomZonedDateTime
        )
        val secondShipment = ShipmentGenerator.createShipment(
            status = ShipmentStatus.AVIZO,
            pickUpDate = randomZonedDateTime.minusDays(1)
        )
        val comparator = createComparator()

        // when
        val order = comparator.compare(firstShipment, secondShipment)

        // then
        Truth.assertThat(order).isAtLeast(1)
    }

    @Test
    fun `when compare is called and status is the same and first pick up date is less then second one then second is bigger`() {
        // given
        val randomZonedDateTime = DateGenerator.MOCK_ZONED_DATE_TIME
        val firstShipment = ShipmentGenerator.createShipment(
            status = ShipmentStatus.AVIZO,
            pickUpDate = randomZonedDateTime
        )
        val secondShipment = ShipmentGenerator.createShipment(
            status = ShipmentStatus.AVIZO,
            pickUpDate = randomZonedDateTime.plusDays(1)
        )
        val comparator = createComparator()

        // when
        val order = comparator.compare(firstShipment, secondShipment)

        // then
        Truth.assertThat(order).isLessThan(0)
    }

    @Test
    fun `when compare is called and status and pick up date is the same and first expiry date is more then second one then first is bigger`() {
        // given
        val randomZonedDateTime = DateGenerator.MOCK_ZONED_DATE_TIME
        val firstShipment = ShipmentGenerator.createShipment(
            status = ShipmentStatus.AVIZO,
            pickUpDate = null,
            expiryDate = randomZonedDateTime
        )
        val secondShipment = ShipmentGenerator.createShipment(
            status = ShipmentStatus.AVIZO,
            pickUpDate = null,
            expiryDate = randomZonedDateTime.minusDays(1)
        )
        val comparator = createComparator()

        // when
        val order = comparator.compare(firstShipment, secondShipment)

        // then
        Truth.assertThat(order).isAtLeast(1)
    }

    @Test
    fun `when compare is called and status and pick up and expiry date is the same and first stored date is less then second one then second is bigger`() {
        // given
        val randomZonedDateTime = DateGenerator.MOCK_ZONED_DATE_TIME
        val firstShipment = ShipmentGenerator.createShipment(
            status = ShipmentStatus.AVIZO,
            pickUpDate = null,
            expiryDate = null,
            storedDate = randomZonedDateTime
        )
        val secondShipment = ShipmentGenerator.createShipment(
            status = ShipmentStatus.AVIZO,
            pickUpDate = null,
            expiryDate = null,
            storedDate = randomZonedDateTime.plusDays(1)
        )
        val comparator = createComparator()

        // when
        val order = comparator.compare(firstShipment, secondShipment)

        // then
        Truth.assertThat(order).isLessThan(0)
    }

    @Test
    fun `when compare is called and status and pick up and expiry date is the same and first stored date is more then second one then first is bigger`() {
        // given
        val firstRandomZonedDateTime = DateGenerator.getRandomZonedDateTime()
        val randomZonedDateTime = DateGenerator.MOCK_ZONED_DATE_TIME
        val firstShipment = ShipmentGenerator.createShipment(
            status = ShipmentStatus.AVIZO,
            pickUpDate = null,
            expiryDate = firstRandomZonedDateTime,
            storedDate = randomZonedDateTime
        )
        val secondShipment = ShipmentGenerator.createShipment(
            status = ShipmentStatus.AVIZO,
            pickUpDate = null,
            expiryDate = firstRandomZonedDateTime,
            storedDate = randomZonedDateTime.minusDays(1)
        )
        val comparator = createComparator()

        // when
        val order = comparator.compare(firstShipment, secondShipment)

        // then
        Truth.assertThat(order).isAtLeast(1)
    }

    @Test
    fun `when compare is called and all date are the same and first number is less then second one then second is bigger`() {
        // given
        val randomZonedDateTime = DateGenerator.MOCK_ZONED_DATE_TIME
        val firstShipment = ShipmentGenerator.createShipment(
            status = ShipmentStatus.AVIZO,
            pickUpDate = null,
            expiryDate = randomZonedDateTime
        )
        val secondShipment = ShipmentGenerator.createShipment(
            status = ShipmentStatus.AVIZO,
            pickUpDate = null,
            expiryDate = randomZonedDateTime.plusDays(1)
        )
        val comparator = createComparator()

        // when
        val order = comparator.compare(firstShipment, secondShipment)

        // then
        Truth.assertThat(order).isLessThan(0)
    }

    @Test
    fun `when compare is called and all dates are the same and first stored date is more then second one then first is bigger`() {
        // given
        val pickUpDate = DateGenerator.getRandomZonedDateTime()
        val expiryDate = DateGenerator.getRandomZonedDateTime()
        val storedDate = DateGenerator.getRandomZonedDateTime()

        val number = 15L
        val firstShipment = ShipmentGenerator.createShipment(
            status = ShipmentStatus.AVIZO,
            pickUpDate = pickUpDate,
            expiryDate = expiryDate,
            storedDate = storedDate,
            number = number.toString()
        )
        val secondShipment = ShipmentGenerator.createShipment(
            status = ShipmentStatus.AVIZO,
            pickUpDate = pickUpDate,
            expiryDate = expiryDate,
            storedDate = storedDate,
            number = (number - 1).toString()
        )
        val comparator = createComparator()

        // when
        val order = comparator.compare(firstShipment, secondShipment)

        // then
        Truth.assertThat(order).isAtLeast(1)
    }

    @Test
    fun `when compare is called and status and pick up date is the same and first number is less then second one then second is bigger`() {
        // given
        val pickUpDate = DateGenerator.getRandomZonedDateTime()
        val expiryDate = DateGenerator.getRandomZonedDateTime()
        val storedDate = DateGenerator.getRandomZonedDateTime()

        val number = 15L
        val firstShipment = ShipmentGenerator.createShipment(
            status = ShipmentStatus.AVIZO,
            pickUpDate = pickUpDate,
            expiryDate = expiryDate,
            storedDate = storedDate,
            number = number.toString()
        )
        val secondShipment = ShipmentGenerator.createShipment(
            status = ShipmentStatus.AVIZO,
            pickUpDate = pickUpDate,
            expiryDate = expiryDate,
            storedDate = storedDate,
            number = (number + 1).toString()
        )
        val comparator = createComparator()

        // when
        val order = comparator.compare(firstShipment, secondShipment)

        // then
        Truth.assertThat(order).isLessThan(0)
    }
}