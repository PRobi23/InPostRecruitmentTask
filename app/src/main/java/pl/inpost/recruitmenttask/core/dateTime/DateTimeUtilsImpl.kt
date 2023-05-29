package pl.inpost.recruitmenttask.core.dateTime

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class DateTimeUtilsImpl : DateTimeUtils {

    /***
     * @inheritDoc
     */
    override fun zonedDateTimeToEpochMilliseconds(zonedDateTime: ZonedDateTime): Long =
        zonedDateTime.toInstant().toEpochMilli()

    /**
     * {@inheritDoc}
     */
    override fun epochMillisecondsToZonedDateTime(
        epochMilliseconds: Long,
        zoneId: ZoneId
    ): ZonedDateTime =
        Instant.ofEpochMilli(epochMilliseconds).atZone(ZoneId.systemDefault())
}