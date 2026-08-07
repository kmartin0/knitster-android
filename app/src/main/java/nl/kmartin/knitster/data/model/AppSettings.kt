package nl.kmartin.knitster.data.model

import nl.kmartin.knitster.theme.ThemeColor
import nl.kmartin.knitster.theme.ThemeMode

data class AppSettings(
    val themeColor: ThemeColor = ThemeColor.DEFAULT,
    val themeMode: ThemeMode = ThemeMode.DEFAULT,
)