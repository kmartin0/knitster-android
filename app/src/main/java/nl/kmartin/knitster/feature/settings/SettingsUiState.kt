package nl.kmartin.knitster.feature.settings

import nl.kmartin.knitster.data.model.AppSettings
import nl.kmartin.knitster.ui.UiText

data class SettingsUiState(
    val settingsErrorMsg: UiText? = null,
    val settings: AppSettings? = null
)
