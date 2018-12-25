package ffc.app.util.datetime

import org.joda.time.LocalDate
import java.util.Calendar

fun Calendar.toLocalDate(): LocalDate? {
    return LocalDate.fromCalendarFields(this)
}

fun LocalDate.toCalendar(): Calendar {
    return Calendar.getInstance().apply {
        set(year, monthOfYear - 1, dayOfMonth)
    }
}
