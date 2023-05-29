package pl.inpost.recruitmenttask.domain.mapper

import pl.inpost.recruitmenttask.domain.data.DateType
import java.time.ZonedDateTime

fun getDateType(
    expiryDate: ZonedDateTime?,
    storedDate: ZonedDateTime?,
    pickUpDate: ZonedDateTime?
): DateType {
    return when {
        pickUpDate != null -> DateType.PICK_UP
        expiryDate != null -> DateType.EXPIRY
        storedDate != null -> DateType.STORED
        else -> DateType.NO_DATE
    }
}