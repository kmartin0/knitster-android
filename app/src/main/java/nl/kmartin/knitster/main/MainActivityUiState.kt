package nl.kmartin.knitster.main

import nl.kmartin.knitster.theme.ThemeColor
import nl.kmartin.knitster.theme.ThemeMode

data class MainActivityUiState(
    val themeColor: ThemeColor = ThemeColor.DEFAULT,
    val themeMode: ThemeMode = ThemeMode.DEFAULT,
    val themeLoaded: Boolean = false
)