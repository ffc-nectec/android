package ffc.app.util.datetime

import org.joda.time.LocalDate
import java.util.*

fun Calendar.toLocalDate(): LocalDate? {
    return LocalDate.fromCalendarFields(this)
}
