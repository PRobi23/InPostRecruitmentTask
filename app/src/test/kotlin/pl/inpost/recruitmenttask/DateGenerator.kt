package pl.inpost.recruitmenttask

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.random.Random

object DateGenerator {
    fun getRandomZonedDateTime(): ZonedDateTime {
        val randomDateTime = LocalDateTime.of(
            Random.nextInt(1998, 2023),    // Year (up to 9999)
            Random.nextInt(12) + 1,  // Month (1-12)
            Random.nextInt(28) + 1,  // Day (1-28)
            Random.nextInt(24),      // Hour (0-23)
            Random.nextInt(60)       // Minute (0-59)
        )

        val zoneIds = ZoneId.getAvailableZoneIds().toList()
        val randomZoneId = ZoneId.of(zoneIds[Random.nextInt(zoneIds.size)])

        return randomDateTime.atZone(randomZoneId)
    }
}