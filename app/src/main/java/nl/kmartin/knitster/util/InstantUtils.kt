package nl.kmartin.knitster.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

fun Instant.toFormattedLastSavedString(locale: Locale): String {
    val formatter = DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.MEDIUM)
        .withLocale(locale)

    return atZone(ZoneId.systemDefault())
        .format(formatter)
}