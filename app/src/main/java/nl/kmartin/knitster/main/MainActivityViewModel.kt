package nl.kmartin.knitster.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import nl.kmartin.knitster.data.repository.ThemeRepository
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    themeRepository: ThemeRepository
) : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> =
        combine(
            themeRepository.observeThemeColor,
            themeRepository.observeThemeMode,
        ) { color, mode ->
            MainActivityUiState(
                themeColor = color,
                themeMode = mode,
                themeLoaded = true
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MainActivityUiState(),
        )
}