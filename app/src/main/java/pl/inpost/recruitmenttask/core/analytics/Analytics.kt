package pl.inpost.recruitmenttask.core.analytics

/**
 * This interface is responsible for analytics.
 */
interface Analytics {

    fun logError()

    fun logUserEvent()
}