package nl.hva.task02.util

import java.util.Calendar
import java.util.Date

/**
 * Helper methods to create dates and check if a date is valid
 */
class DateUtils {
    private val calendar = Calendar.getInstance()

    // this method is needed since the constructor Date(y, m, d) is deprecated
    fun createDate(year: Int, month: Int, day: Int): Date {
        calendar.set(year, month, day)
        return calendar.time
    }

    // very basic date validation (user could still type 31 Feb 2020)
    fun isValidDate(year: Int, month: Int, day: Int): Boolean {
        if (year in 1950..2022 &&
            month in 1..12 &&
            day in 1..31
        ) {
            return true
        }

        return false
    }
}