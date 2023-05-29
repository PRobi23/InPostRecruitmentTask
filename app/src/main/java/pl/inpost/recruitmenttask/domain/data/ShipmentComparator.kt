package pl.inpost.recruitmenttask.domain.data

import java.time.ZonedDateTime
import javax.inject.Inject

class ShipmentComparator @Inject constructor() : Comparator<Shipment> {
    override fun compare(firstShipment: Shipment, secondShipment: Shipment): Int {
        // Compare the status (order defined in ShipmentStatus.kt)
        val statusComparison = firstShipment.status.compareTo(secondShipment.status)
        if (statusComparison != 0) {
            return statusComparison
        }

        // Compare the pickup date
        val pickupDateComparison = compareDates(firstShipment.pickUpDate, secondShipment.pickUpDate)
        if (pickupDateComparison != 0) {
            return pickupDateComparison
        }

        // Compare the expiry date
        val expiryDateComparison = compareDates(firstShipment.expiryDate, secondShipment.expiryDate)
        if (expiryDateComparison != 0) {
            return expiryDateComparison
        }

        // Compare the stored date
        val storedDateComparison = compareDates(firstShipment.storedDate, secondShipment.storedDate)
        if (storedDateComparison != 0) {
            return storedDateComparison
        }

        // Compare the number
        return firstShipment.number.compareTo(secondShipment.number)
    }

    private fun compareDates(date1: ZonedDateTime?, date2: ZonedDateTime?): Int {
        return if (date1 == null && date2 == null) {
            0
        } else if (date1 != null && date2 != null) {
            date1.compareTo(date2)
        } else if (date1 != null) {
            // date1 is not null, date2 is null
            1
        } else {
            // date1 is null, date2 is not null
            -1
        }
    }
}
