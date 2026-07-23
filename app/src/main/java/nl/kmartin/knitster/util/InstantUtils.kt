package nl.kmartin.knitster.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val lastSavedFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd-MM-yyyy 'at' HH:mm:ss")

/**
 * Formats this instant for display as the project's last saved timestamp.
 */
fun Instant.toFormattedLastSavedString(): String {
    return this.atZone(ZoneId.systemDefault())
        .format(lastSavedFormatter)
}