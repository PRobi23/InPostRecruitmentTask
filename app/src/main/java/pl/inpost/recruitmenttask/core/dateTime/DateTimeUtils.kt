package pl.inpost.recruitmenttask.core.dateTime

import java.time.ZoneId
import java.time.ZonedDateTime

/***
 * Helper class to do utility function in date time class
 */
interface DateTimeUtils {

    /***
     * Creates epoch milliseconds from zoned date time
     */
    fun zonedDateTimeToEpochMilliseconds(zonedDateTime: ZonedDateTime): Long

    /***
     * Creates zoned date time from epoch milliseconds
     */
    fun epochMillisecondsToZonedDateTime(
        epochMilliseconds: Long,
        zoneId: ZoneId = ZoneId.systemDefault()
    ): ZonedDateTime
}