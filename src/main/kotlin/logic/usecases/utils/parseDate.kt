package org.beijing.logic.usecases.utils

import kotlinx.datetime.LocalDate

fun String.parseDate(): LocalDate {
    try {
        return LocalDate.parse(this)
    } catch (exception: Exception) {
        throw IllegalArgumentException("❌ Invalid date format. Please use (YYYY-MM-DD).")
    }
}